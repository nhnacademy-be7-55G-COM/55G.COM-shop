package shop.s5g.shop.repository.publisher;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.Publisher;
import shop.s5g.shop.repository.publisher.qdsl.PublisherQuerydslRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long>, PublisherQuerydslRepository {
    boolean existsByNameAndActive(String name, boolean active);
}
