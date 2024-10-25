package shop.S5G.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.S5G.shop.dto.category.CategoryRequestDto;
import shop.S5G.shop.entity.Category;
import shop.S5G.shop.exception.CategoryException.CategoryBadRequestException;
import shop.S5G.shop.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    //카테고리 등록
    @PostMapping("/category")
    public ResponseEntity addCategory(@Validated @RequestBody CategoryRequestDto categorydto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new CategoryBadRequestException("잘못된 입력입니다.");
        }
        Category category = new Category(categorydto.getCategoryName(), categorydto.isActive());
        categoryService.createCategory(category);
        return ResponseEntity.ok().build();
    }

    //카테고리 목록 조회
    @GetMapping("/category")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.allCategory();
        return ResponseEntity.ok().body(categories);
    }

    //카테고리 수정
    @PutMapping("/category/{categoryId}")
    public ResponseEntity updateCategory(@Validated @PathVariable("categoryId") Long categoryId, @Validated @RequestBody CategoryRequestDto categorydto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new CategoryBadRequestException("잘못된 입력입니다.");
        }
        if (categoryId < 1) {
            throw new CategoryBadRequestException("categoryId must be greater than 0");
        }
        Category category = new Category(categorydto.getCategoryName(), categorydto.isActive());
        categoryService.updateCategory(categoryId, category);
        return ResponseEntity.ok().build();
    }
    //카테고리 삭제
    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity deleteCategory(@PathVariable("categoryId") Long categoryId) {
        if (categoryId < 1) {
            throw new CategoryBadRequestException("categoryId must be greater than 0");
        }
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }
}
