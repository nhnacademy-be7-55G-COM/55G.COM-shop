package shop.s5g.shop.service.bookcategory;

import shop.s5g.shop.dto.bookcategory.BookCategoryResponseDto;

import java.util.List;

public interface BookCategoryService {
    void addCategoryInBook(Long bookId, Long categoryId);

    List<BookCategoryResponseDto> getCategoryInBook(Long bookId);

    void deleteCategory(Long bookId, Long categoryId);
}
