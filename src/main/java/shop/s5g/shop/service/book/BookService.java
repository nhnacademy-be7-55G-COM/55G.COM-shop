package shop.s5g.shop.service.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.book.BookDetailResponseDto;
import shop.s5g.shop.dto.book.BookPageableResponseDto;
import shop.s5g.shop.dto.book.BookRequestDto;
import shop.s5g.shop.dto.book.BookResponseDto;
import shop.s5g.shop.dto.book.category.BookCategoryBookResponseDto;

import java.util.List;
import shop.s5g.shop.dto.book.BookSimpleResponseDto;

public interface BookService {
    void createBook(BookRequestDto bookDto);
    BookDetailResponseDto getBookById(Long bookId);
    void updateBooks(Long bookId, BookRequestDto bookDto);
    void deleteBooks(Long bookId);
    Page<BookPageableResponseDto> allBookPageable(Pageable pageable);

    List<BookSimpleResponseDto> getSimpleBooks(List<Long> bookIdList);


}
