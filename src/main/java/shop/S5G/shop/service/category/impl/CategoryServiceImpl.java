package shop.S5G.shop.service.category.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.S5G.shop.dto.category.CategoryRequestDto;
import shop.S5G.shop.dto.category.CategoryResponseDto;
import shop.S5G.shop.entity.Category;
import shop.S5G.shop.exception.CategoryException.CategoryResourceNotFoundException;
import shop.S5G.shop.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryServiceImpl {

    private final CategoryRepository categoryRepository;
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    //카테고리 등록
    public void createCategory(CategoryRequestDto categorydto) {
        Category category = new Category(categorydto.parentCategory(), categorydto.categoryName(), categorydto.active());

        categoryRepository.save(category);
    }

    //모든 카테고리 조회
    public List<CategoryResponseDto> allCategory() {

        return categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryResponseDto(
                        category.getParentCategory(),
                        category.getCategoryName(),
                        category.isActive()
                ))
                .toList();
    }

    //카테고리 수정
    public void updateCategory(Long categoryId, CategoryRequestDto categoryDto) {
        Category category = new Category(categoryDto.categoryName(), categoryDto.active());

        Category category1 = categoryRepository.findById(categoryId)
                .orElseThrow(()->new CategoryResourceNotFoundException("해당 카테고리가 없습니다."));

        category1.setCategoryId(category.getCategoryId());
        category1.setParentCategory(category.getParentCategory());
        category1.setCategoryName(category.getCategoryName());
        category1.setActive(category.isActive());

        categoryRepository.save(category1);
    }

    //카테고리 삭제
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryResourceNotFoundException("Category with id " + categoryId + " not found");
        }
        categoryRepository.deleteById(categoryId);
    }
}