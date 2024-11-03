package shop.S5G.shop.service.category;

import shop.S5G.shop.dto.category.CategoryRequestDto;
import shop.S5G.shop.dto.category.CategoryResponseDto;
import shop.S5G.shop.dto.category.CategoryUpdateRequestDto;

import java.util.List;

public interface CategoryService {
    public void createCategory(CategoryRequestDto categorydto);
    public List<CategoryResponseDto> allCategory();
    public void updateCategory(Long categoryId, CategoryUpdateRequestDto categoryDto);
    public void deleteCategory(Long categoryId);
}
