package shop.s5g.shop.repository.bookcategory.qdsl;

import shop.s5g.shop.dto.book.BookDetailResponseDto;
import shop.s5g.shop.dto.book.BookPageableResponseDto;
import shop.s5g.shop.dto.bookCategory.BookCategoryBookResponseDto;
import shop.s5g.shop.dto.bookCategory.BookCategoryResponseDto;

import java.util.List;

public interface BookCategoryQuerydslRepository {
    List<BookCategoryResponseDto> findCategoryByBookId(Long bookId);

    List<BookCategoryBookResponseDto> findBookByCategoryId(Long categoryId);

    List<BookCategoryResponseDto> findAllBookCategory();

    List<BookPageableResponseDto> getBookList(Long categoryId);
}
