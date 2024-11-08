package shop.S5G.shop.service.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.S5G.shop.dto.book.BookPageableResponseDto;
import shop.S5G.shop.dto.book.BookRequestDto;
import shop.S5G.shop.dto.book.BookResponseDto;

import java.util.List;

public interface BookService {
    void createBook(BookRequestDto bookDto);
    List<BookResponseDto> allBook();
    BookResponseDto getBookById(Long bookId);
    void updateBooks(Long bookId, BookRequestDto bookDto);
    void deleteBooks(Long bookId);

    Page<BookPageableResponseDto> allBookPageable(Pageable pageable);
}
