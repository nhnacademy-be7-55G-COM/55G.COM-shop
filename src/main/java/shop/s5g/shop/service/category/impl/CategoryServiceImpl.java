package shop.s5g.shop.service.category.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.category.CategoryRequestDto;
import shop.s5g.shop.dto.category.CategoryResponseDto;
import shop.s5g.shop.dto.category.CategoryUpdateRequestDto;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.exception.category.CategoryResourceNotFoundException;
import shop.s5g.shop.repository.category.CategoryRepository;
import shop.s5g.shop.service.category.CategoryService;

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

        //이름으로 카테고리 조회
        Category p_category = categoryRepository.findByCategoryName(categoryDto.categoryName());

        Category category = new Category(p_category, categoryDto.categoryName(), categoryDto.active());
        categoryRepository.save(category);
    }

    //모든 카테고리 조회
    public List<CategoryResponseDto> allCategory() {
        return categoryRepository.getAllCategory();
    }

    //자식 카테고리 조회
    @Override
    public List<CategoryResponseDto> getChildCategory(Long categoryId) {
        return categoryRepository.getChild_Category(categoryId);
    }

    //국내도서 하위 카테고리
    @Override
    public List<CategoryResponseDto> getKoreaBooks() {
        return categoryRepository.getKoreaBook();
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