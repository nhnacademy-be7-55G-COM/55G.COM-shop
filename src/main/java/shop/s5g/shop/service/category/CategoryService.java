package shop.s5g.shop.service.category;

import shop.s5g.shop.dto.category.CategoryRequestDto;
import shop.s5g.shop.dto.category.CategoryResponseDto;
import shop.s5g.shop.dto.category.CategoryUpdateRequestDto;

import java.util.List;

public interface CategoryService {
    public void createCategory(CategoryRequestDto categorydto);
    public List<CategoryResponseDto> allCategory();
    public void updateCategory(Long categoryId, CategoryUpdateRequestDto categoryDto);
    public void deleteCategory(Long categoryId);
}
