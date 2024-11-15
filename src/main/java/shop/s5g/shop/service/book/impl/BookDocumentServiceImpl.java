package shop.s5g.shop.service.book.impl;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.book.BookDocumentResponseDto;
import shop.s5g.shop.entity.document.BookDocument;
import shop.s5g.shop.service.book.BookDocumentService;

@Service
@RequiredArgsConstructor
public class BookDocumentServiceImpl implements BookDocumentService {

    private final ElasticsearchOperations elasticsearchOperations;

    public List<BookDocumentResponseDto> searchByTitleOrDescription(String keyword) {
        Criteria criteria = new Criteria("title").matches(keyword)
            .or(new Criteria("description").matches(keyword));

        CriteriaQuery query = new CriteriaQuery(criteria);

        SearchHits<BookDocument> searchHits = elasticsearchOperations.search(query,
            BookDocument.class);

        return searchHits.getSearchHits().stream()
            .map(hit -> BookDocumentResponseDto.toDto(hit.getContent()))
            .collect(Collectors.toList());
    }
}
