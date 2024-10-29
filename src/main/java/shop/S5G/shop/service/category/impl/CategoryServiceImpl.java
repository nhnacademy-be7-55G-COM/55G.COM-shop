package shop.S5G.shop.service.category.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.S5G.shop.dto.category.CategoryRequestDto;
import shop.S5G.shop.dto.category.CategoryResponseDto;
import shop.S5G.shop.entity.Category;
import shop.S5G.shop.exception.CategoryException.CategoryAlreadyExistsException;
import shop.S5G.shop.exception.CategoryException.CategoryResourceNotFoundException;
import shop.S5G.shop.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl {
    @Autowired
    private CategoryRepository categoryRepository;

    //카테고리 등록
    public void createCategory(CategoryRequestDto categorydto) {
        Category category = new Category(categorydto.getCategoryId(), categorydto.getParentCategory(), categorydto.getCategoryName(), categorydto.isActive());

        Optional<Category> id = categoryRepository.findById(categorydto.getCategoryId());
        if(id.isPresent()) {
            throw new CategoryAlreadyExistsException("Category already exists");
        }
        categoryRepository.save(category);
    }

    //모든 카테고리 조회
    public List<CategoryResponseDto> allCategory() {
        List<Category> category = categoryRepository.findAll();
        List<CategoryResponseDto> categoryResponseDto = new ArrayList<>();
        for(int i=0 ; i<category.size() ; i++) {
            categoryResponseDto.get(i).setParentCategory(category.get(i).getParentCategory());
            categoryResponseDto.get(i).setCategoryName(category.get(i).getCategoryName());
            categoryResponseDto.get(i).setActive(category.get(i).isActive());
        }
        return categoryResponseDto;
    }

    //카테고리 수정
    public void updateCategory(Long categoryId, CategoryRequestDto categoryDto) {
        Category category = new Category(categoryDto.getCategoryName(), categoryDto.isActive());

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