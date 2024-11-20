package shop.s5g.shop.service.category;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.category.CategoryDetailResponseDto;
import shop.s5g.shop.dto.category.CategoryRequestDto;
import shop.s5g.shop.dto.category.CategoryResponseDto;
import shop.s5g.shop.dto.category.CategoryUpdateRequestDto;

import java.util.List;

public interface CategoryService {
    void createCategory(CategoryRequestDto categorydto);
    Page<CategoryResponseDto> allCategory(Pageable pageable);
    void updateCategory(Long categoryId, CategoryUpdateRequestDto categoryDto);
    void deleteCategory(Long categoryId);

    List<CategoryResponseDto> getChildCategory(long categoryId);

    List<CategoryResponseDto> getKoreaBooks();

    CategoryResponseDto getCategory(Long categoryId);

    List<CategoryDetailResponseDto> getCategoryDetail(Long categoryId);
}
