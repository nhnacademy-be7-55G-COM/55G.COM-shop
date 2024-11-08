package shop.S5G.shop.repository.bookstatus;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.BookStatus;
import shop.S5G.shop.repository.bookstatus.qdsl.BookStatusQuerydslRepository;

public interface BookStatusRepository extends JpaRepository<BookStatus, Long>, BookStatusQuerydslRepository {
}
