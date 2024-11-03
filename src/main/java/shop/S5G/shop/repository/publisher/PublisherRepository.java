package shop.S5G.shop.repository.publisher;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.Publisher;
import shop.S5G.shop.repository.publisher.qdsl.PublisherQuerydslRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Long>, PublisherQuerydslRepository {
    boolean existsByNameAndActive(String name, boolean active);
}
