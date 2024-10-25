package shop.S5G.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByBookIdIn(List<Long> bookId);
}
