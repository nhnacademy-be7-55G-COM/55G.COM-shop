package shop.S5G.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.S5G.shop.entity.Category;
import shop.S5G.shop.exception.CategoryException.CategoryAlreadyExistsException;
import shop.S5G.shop.exception.CategoryException.CategoryResourceNotFoundException;
import shop.S5G.shop.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    //카테고리 등록
    public void createCategory(Category category) {
        Optional<Category> id = categoryRepository.findById(category.getCategoryId());
        if(id.isPresent()) {
            throw new CategoryAlreadyExistsException("Category already exists");
        }
        categoryRepository.save(category);
    }

    //모든 카테고리 조회
    public List<Category> allCategory() {
        return categoryRepository.findAll();
    }

    //카테고리 수정
    public void updateCategory(Long categoryId, Category category) {
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