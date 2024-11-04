package shop.S5G.shop.repository.book.qdsl;

import shop.S5G.shop.dto.Book.BookRequestDto;
import shop.S5G.shop.dto.Book.BookResponseDto;

import java.util.List;

public interface BookQuerydslRepository {
    void updateBook(long bookId, BookRequestDto bookDto);
    List<BookResponseDto> findAllBookList();
    BookResponseDto getBookDetail(long bookId);
}
