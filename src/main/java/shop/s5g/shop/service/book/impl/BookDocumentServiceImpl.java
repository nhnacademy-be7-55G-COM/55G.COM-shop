package shop.s5g.shop.service.book.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.book.BookDocumentResponseDto;
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

    public PageResponseDto<BookDocumentResponseDto> searchByTitleOrDescription(String keyword,
        Pageable pageable) {
        Criteria criteria = new Criteria("title").matches(keyword)
            .or(new Criteria("description").matches(keyword));

        CriteriaQuery query = new CriteriaQuery(criteria);
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
}
