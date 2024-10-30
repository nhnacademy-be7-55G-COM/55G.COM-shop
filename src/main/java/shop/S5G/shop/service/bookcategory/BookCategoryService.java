package shop.S5G.shop.service.bookcategory;

import shop.S5G.shop.dto.bookcategory.BookCategoryResponseDto;

import java.util.List;

public interface BookCategoryService {
    void addCategoryInBook(Long bookId, Long categoryId);

    List<BookCategoryResponseDto> getCategoryInBook(Long bookId);

    void deleteCategory(Long bookId, Long categoryId);
}
