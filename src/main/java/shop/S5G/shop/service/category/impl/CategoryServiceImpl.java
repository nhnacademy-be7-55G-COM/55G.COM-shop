package shop.S5G.shop.service.category.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.S5G.shop.dto.category.CategoryRequestDto;
import shop.S5G.shop.dto.category.CategoryResponseDto;
import shop.S5G.shop.dto.category.CategoryUpdateRequestDto;
import shop.S5G.shop.entity.Category;
import shop.S5G.shop.exception.category.CategoryResourceNotFoundException;
import shop.S5G.shop.repository.category.CategoryRepository;
import shop.S5G.shop.service.category.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    //카테고리 등록
    public void createCategory(CategoryRequestDto categoryDto) {

        Category p_category = categoryRepository.findById(categoryDto.parentCategory()).orElseThrow(() -> new CategoryResourceNotFoundException("해당 카테고리는 존재하지 않습니다."));
        Category category = new Category(p_category, categoryDto.categoryName(), categoryDto.active());

        categoryRepository.save(category);
    }

    //모든 카테고리 조회
    public List<CategoryResponseDto> allCategory() {
        return categoryRepository.getAllCategory();
    }

    //카테고리 수정
    public void updateCategory(Long categoryId, CategoryUpdateRequestDto categoryDto) {
        if(categoryRepository.findById(categoryId).isPresent()) {
            throw new CategoryResourceNotFoundException(categoryId + " 는 존재하지 않습니다.");
        }

        categoryRepository.updatesCategory(categoryId, categoryDto);
    }

    //카테고리 삭제(비활성화)
    public void deleteCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryResourceNotFoundException("Category with id " + categoryId + " not found");
        }
        categoryRepository.inactiveCategory(categoryId);
    }
}