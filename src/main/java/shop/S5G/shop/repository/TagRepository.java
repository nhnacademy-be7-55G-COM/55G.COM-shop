package shop.S5G.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByTagNameAndActive(String name, boolean active);
}
