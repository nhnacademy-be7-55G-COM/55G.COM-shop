package shop.S5G.shop.service.BookCategory;

import shop.S5G.shop.dto.bookcategory.BookCategoryResponseDto;
import shop.S5G.shop.dto.category.CategoryUpdateInBookResponseDto;
import shop.S5G.shop.entity.BookCategory.BookCategory;
import shop.S5G.shop.entity.Category;

import java.util.List;

public interface BookCategoryService {
    void addCategoryInBook(Long bookId, Long categoryId);

    List<BookCategoryResponseDto> getCategoryInBook(Long bookId);

    void deleteCategory(Long bookId, Long categoryId);
}
