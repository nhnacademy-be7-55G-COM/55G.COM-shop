package shop.S5G.shop.repository.book;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.dto.Book.BookRequestDto;
import shop.S5G.shop.dto.Book.BookResponseDto;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.repository.book.qdsl.BookQuerydslRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long>, BookQuerydslRepository {
    List<BookResponseDto> findAllByBookIdIn(List<Long> bookId);
    List<BookResponseDto> findAllAsDto();

    boolean existsAllByBookIdIn(List<Long> bookId);

}
