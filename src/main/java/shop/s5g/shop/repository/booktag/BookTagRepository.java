package shop.s5g.shop.repository.booktag;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.booktag.BookTag;
import shop.s5g.shop.entity.booktag.BookTagId;
import shop.s5g.shop.repository.booktag.qdsl.BookTagQuerydslRepository;

public interface BookTagRepository extends JpaRepository<BookTag, BookTagId>, BookTagQuerydslRepository {
}
