package shop.s5g.shop.service;

import java.util.List;
import shop.s5g.shop.dto.book.BookDetailResponseDto;
import shop.s5g.shop.dto.book.BookRequestDto;
import shop.s5g.shop.entity.Book;

public interface BookService {
    public void createBook(BookRequestDto bookRequestDto);
    public List<BookDetailResponseDto> allBook();
    public BookDetailResponseDto getBookById(Long bookId);
    public void updateBooks(Long bookId, BookRequestDto book);
    public void deleteBooks(Long bookId);
    public List<Book> findAllByBookIds(List<Long> bookIds);
}
