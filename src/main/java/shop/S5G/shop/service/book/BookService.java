package shop.S5G.shop.service.book;

import shop.S5G.shop.dto.Book.BookRequestDto;
import shop.S5G.shop.dto.Book.BookResponseDto;
import shop.S5G.shop.entity.Book;

import java.util.List;

public interface BookService {
    public void createBook(BookRequestDto bookDto);
    public List<BookResponseDto> allBook();
    public BookResponseDto getBookById(Long bookId);
    public void updateBooks(Long bookId, BookRequestDto bookDto);
    public void deleteBooks(Long bookId);
    public List<Book> findAllByBookIds(List<Long> bookIds);
}
