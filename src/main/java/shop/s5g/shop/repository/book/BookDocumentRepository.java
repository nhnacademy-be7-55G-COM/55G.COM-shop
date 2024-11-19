package shop.s5g.shop.repository.book;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import shop.s5g.shop.entity.document.BookDocument;

public interface BookDocumentRepository extends ElasticsearchRepository<BookDocument, Long> {

}
