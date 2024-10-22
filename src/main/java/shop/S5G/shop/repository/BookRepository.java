package shop.S5G.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.Book;

public interface BookRepository extends JpaRepository<Book, Integer> {
}
