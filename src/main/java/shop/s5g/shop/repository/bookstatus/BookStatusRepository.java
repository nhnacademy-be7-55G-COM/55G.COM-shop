package shop.s5g.shop.repository.bookstatus;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.BookStatus;

public interface BookStatusRepository extends JpaRepository<BookStatus, Long> {
}
