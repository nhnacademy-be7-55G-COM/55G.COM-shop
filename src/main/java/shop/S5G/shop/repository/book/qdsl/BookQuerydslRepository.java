package shop.S5G.shop.repository.book.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.S5G.shop.dto.book.BookPageableResponseDto;
import shop.S5G.shop.dto.book.BookRequestDto;
import shop.S5G.shop.dto.book.BookResponseDto;

import java.util.List;

public interface BookQuerydslRepository {
    void updateBook(long bookId, BookRequestDto bookDto);
    List<BookResponseDto> findAllBookList();
    BookResponseDto getBookDetail(long bookId);
    Page<BookPageableResponseDto> findAllBookPage(Pageable pageable);
}
