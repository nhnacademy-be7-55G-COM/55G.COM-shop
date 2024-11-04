package shop.S5G.shop.repository.category.qdsl;

import shop.S5G.shop.dto.category.CategoryRequestDto;
import shop.S5G.shop.dto.category.CategoryResponseDto;
import shop.S5G.shop.dto.category.CategoryUpdateRequestDto;

import java.util.List;

public interface CategoryQuerydslRepository {
    void updatesCategory(Long categoryId, CategoryUpdateRequestDto categoryDto);
    List<CategoryResponseDto> getAllCategory();
    void inactiveCategory(Long categoryId);
}
