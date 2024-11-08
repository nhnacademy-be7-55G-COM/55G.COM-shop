package shop.s5g.shop.service.book;

import shop.s5g.shop.dto.Book.BookRequestDto;
import shop.s5g.shop.dto.Book.BookResponseDto;

import java.util.List;

public interface BookService {
    void createBook(BookRequestDto bookDto);
    List<BookResponseDto> allBook();
    BookResponseDto getBookById(Long bookId);
    void updateBooks(Long bookId, BookRequestDto bookDto);
    void deleteBooks(Long bookId);
}
