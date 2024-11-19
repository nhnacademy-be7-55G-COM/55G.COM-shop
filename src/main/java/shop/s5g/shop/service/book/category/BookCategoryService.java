package shop.s5g.shop.service.book.category;

import shop.s5g.shop.dto.book.category.BookCategoryResponseDto;
import shop.s5g.shop.dto.book.category.BookCategoryBookResponseDto;

import java.util.List;

public interface BookCategoryService {
    void addCategoryInBook(Long bookId, Long categoryId);

    List<BookCategoryResponseDto> getCategoryInBook(Long bookId);

    void deleteCategory(Long bookId, Long categoryId);

    List<BookCategoryBookResponseDto> getBookByCategoryId(Long categoryId);

    List<BookCategoryResponseDto> getAllBookCategory();
}
