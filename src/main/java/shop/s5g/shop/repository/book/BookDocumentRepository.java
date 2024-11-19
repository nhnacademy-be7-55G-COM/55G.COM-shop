package shop.s5g.shop.repository.book;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import shop.s5g.shop.entity.document.BookDocument;

@ConditionalOnProperty(
    name = "spring.data.elasticsearch.repositories.enabled",
    havingValue = "true",
    matchIfMissing = false
)
public interface BookDocumentRepository extends ElasticsearchRepository<BookDocument, Long> {

}
