package shop.s5g.shop.service.book.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.book.BookDocumentResponseDto;
import shop.s5g.shop.elasticsearch.BoolQueryBuilder;
import shop.s5g.shop.entity.document.BookDocument;
import shop.s5g.shop.repository.book.BookDocumentRepository;
import shop.s5g.shop.service.book.BookDocumentService;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
    name = "spring.data.elasticsearch.repositories.enabled",
    havingValue = "true"
)
public class BookDocumentServiceImpl implements BookDocumentService {

    private final BookDocumentRepository bookDocumentRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public PageResponseDto<BookDocumentResponseDto> findAllBooks(Pageable pageable) {
        Page<BookDocument> bookDocumentPage = bookDocumentRepository.findAll(pageable);

        List<BookDocumentResponseDto> bookList = bookDocumentPage.getContent().stream()
            .map(BookDocumentResponseDto::toDto)
            .toList();

        return new PageResponseDto<>(bookList, bookDocumentPage.getTotalPages(),
            bookDocumentPage.getSize(), bookDocumentPage.getTotalElements());
    }

    public PageResponseDto<BookDocumentResponseDto> searchByKeyword(String keyword,
        Pageable pageable) {

        Criteria titleCriteria = new Criteria("title").matches(keyword).boost(60.f);
        Criteria descriptionCriteria = new Criteria("description").matches(keyword).boost(30.f);
        Criteria tagCriteria = new Criteria("tag_names.keyword").is(keyword).boost(50.f);
        Criteria authorCriteria = new Criteria("author_names").matches(keyword).boost(50.f);
        Criteria publisherCriteria = new Criteria("publisher_name").matches(keyword).boost(20.f);

        Criteria combinedCriteria = titleCriteria.or(descriptionCriteria).or(tagCriteria)
            .or(authorCriteria).or(publisherCriteria);

        CriteriaQuery query = new CriteriaQuery(combinedCriteria);
        query.setPageable(pageable);

        SearchHits<BookDocument> searchHits = elasticsearchOperations.search(query,
            BookDocument.class);

        List<BookDocumentResponseDto> searchBookList = searchHits.getSearchHits().stream()
            .map(hit -> BookDocumentResponseDto.toDto(hit.getContent()))
            .toList();

        Page<BookDocumentResponseDto> page = new PageImpl<>(searchBookList, pageable,
            searchHits.getTotalHits());
        return PageResponseDto.of(page);
    }

    @Override
    public PageResponseDto<BookDocumentResponseDto> findAllBooksByCategory(String categoryName,
        Pageable pageable) {

        Criteria categoryCriteria = new Criteria("category_names.keyword").is(categoryName);

        CriteriaQuery query = new CriteriaQuery(categoryCriteria);
        query.setPageable(pageable);

        SearchHits<BookDocument> searchHits = elasticsearchOperations.search(query,
            BookDocument.class);

        List<BookDocumentResponseDto> searchBookList = searchHits.getSearchHits().stream()
            .map(hit -> BookDocumentResponseDto.toDto(hit.getContent()))
            .toList();

        Page<BookDocumentResponseDto> page = new PageImpl<>(searchBookList, pageable,
            searchHits.getTotalHits());

        return PageResponseDto.of(page);
    }

    @Override
    public PageResponseDto<BookDocumentResponseDto> searchByCategoryAndKeyword(String categoryName,
        String keyword, Pageable pageable) {

        Query finalQuery = createByCategoryAndKeywordQuery(categoryName, keyword);

        NativeQuery query = new NativeQuery(finalQuery);
        query.setPageable(pageable);

        SearchHits<BookDocument> searchHits = elasticsearchOperations.search(query,
            BookDocument.class);

        List<BookDocumentResponseDto> searchBookList = searchHits.getSearchHits().stream()
            .map(hit -> BookDocumentResponseDto.toDto(hit.getContent()))
            .toList();

        Page<BookDocumentResponseDto> page = new PageImpl<>(searchBookList, pageable,
            searchHits.getTotalHits());

        return PageResponseDto.of(page);
    }

    private Query createByCategoryAndKeywordQuery(String categoryName, String keyword) {
        Query categoryQuery = new TermQuery.Builder()
            .field("category_names.keyword")
            .value(categoryName)
            .build()
            ._toQuery();

        List<Query> keywordQueries = new ArrayList<>();
        keywordQueries.add(BoolQueryBuilder.createMatchQuery("title", keyword, 60f));
        keywordQueries.add(BoolQueryBuilder.createMatchQuery("description", keyword, 30f));
        keywordQueries.add(BoolQueryBuilder.createTermQuery("tag_names.keyword", keyword, 50f));
        keywordQueries.add(BoolQueryBuilder.createMatchQuery("author_names", keyword, 50f));
        keywordQueries.add(BoolQueryBuilder.createMatchQuery("publisher_name", keyword, 20f));

        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        boolQueryBuilder.must(categoryQuery);
        boolQueryBuilder.should(keywordQueries);
        boolQueryBuilder.minimumShouldMatch("1");

        return boolQueryBuilder.build()._toQuery();
    }
}
