package shop.s5g.shop.repository.book;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.repository.book.qdsl.BookQuerydslRepository;

public interface BookRepository extends JpaRepository<Book, Long>, BookQuerydslRepository {

    boolean existsAllByBookIdIn(List<Long> bookId);

}
