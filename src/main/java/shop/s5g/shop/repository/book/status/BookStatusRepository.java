package shop.s5g.shop.repository.book.status;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.BookStatus;
import shop.s5g.shop.repository.book.status.qdsl.BookStatusQuerydslRepository;

public interface BookStatusRepository extends JpaRepository<BookStatus, Long>, BookStatusQuerydslRepository {
}
