package shop.s5g.shop.repository.book.category.qdsl;

import shop.s5g.shop.dto.book.category.BookCategoryResponseDto;
import shop.s5g.shop.dto.book.BookDetailResponseDto;
import shop.s5g.shop.dto.book.BookPageableResponseDto;
import shop.s5g.shop.dto.book.category.BookCategoryBookResponseDto;
import shop.s5g.shop.dto.book.category.BookCategoryResponseDto;

import java.util.List;

public interface BookCategoryQuerydslRepository {

    List<BookCategoryResponseDto> findAllBookCategory();

//    List<BookPageableResponseDto> getBookList(Long categoryId);
}
