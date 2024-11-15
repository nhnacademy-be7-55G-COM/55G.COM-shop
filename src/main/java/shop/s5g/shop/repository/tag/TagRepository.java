package shop.s5g.shop.repository.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.Tag;
import shop.s5g.shop.repository.tag.qdsl.TagQuerydslRepository;

public interface TagRepository extends JpaRepository<Tag, Long>, TagQuerydslRepository {
    boolean existsByTagNameAndActive(String name, boolean active);
}
