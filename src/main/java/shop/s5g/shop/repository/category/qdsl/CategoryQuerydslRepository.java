package shop.s5g.shop.repository.category.qdsl;

import shop.s5g.shop.dto.category.CategoryResponseDto;
import shop.s5g.shop.dto.category.CategoryUpdateRequestDto;

import java.util.List;

public interface CategoryQuerydslRepository {
    void updatesCategory(Long categoryId, CategoryUpdateRequestDto categoryDto);
    List<CategoryResponseDto> getAllCategory();
    void inactiveCategory(Long categoryId);

    List<CategoryResponseDto> getChild_Category(Long categoryId);

    List<CategoryResponseDto> getKoreaBook();
}
