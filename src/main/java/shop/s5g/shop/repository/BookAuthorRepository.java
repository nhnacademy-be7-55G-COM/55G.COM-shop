package shop.s5g.shop.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.Author;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.BookAuthor;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {

    List<BookAuthor> findAllByBook(Book book);
    List<BookAuthor> findAllByAuthor(Author author);
}
