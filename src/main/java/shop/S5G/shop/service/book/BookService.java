package shop.S5G.shop.service.book;

import shop.S5G.shop.dto.book.BookRequestDto;
import shop.S5G.shop.dto.book.BookResponseDto;

import java.util.List;

public interface BookService {
    void createBook(BookRequestDto bookDto);
    List<BookResponseDto> allBook();
    BookResponseDto getBookById(Long bookId);
    void updateBooks(Long bookId, BookRequestDto bookDto);
    void deleteBooks(Long bookId);
}
