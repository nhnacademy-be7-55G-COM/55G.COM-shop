package shop.S5G.shop.service.category;

import shop.S5G.shop.dto.category.CategoryRequestDto;
import shop.S5G.shop.dto.category.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    public void createCategory(CategoryRequestDto categorydto);
    public List<CategoryResponseDto> allCategory();
    public void updateCategory(Long categoryId, CategoryRequestDto categoryDto);
    public void deleteCategory(Long categoryId);
}
