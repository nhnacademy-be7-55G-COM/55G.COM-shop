package shop.S5G.shop.repository.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.Tag;
import shop.S5G.shop.repository.tag.qdsl.TagQuerydslRepository;

public interface TagRepository extends JpaRepository<Tag, Long>, TagQuerydslRepository {
    boolean existsByTagNameAndActive(String name, boolean active);
}
