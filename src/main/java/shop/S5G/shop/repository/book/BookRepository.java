package shop.S5G.shop.repository.book;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.repository.book.qdsl.BookQuerydslRepository;

public interface BookRepository extends JpaRepository<Book, Long>, BookQuerydslRepository {

    boolean existsAllByBookIdIn(List<Long> bookId);

}
