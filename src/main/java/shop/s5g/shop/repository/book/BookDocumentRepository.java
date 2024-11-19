package shop.s5g.shop.repository.book;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import shop.s5g.shop.config.ElasticSearchConfig;
import shop.s5g.shop.entity.document.BookDocument;

@ConditionalOnBean(ElasticSearchConfig.class)
public interface BookDocumentRepository extends ElasticsearchRepository<BookDocument, Long> {

}
