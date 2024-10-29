package shop.S5G.shop.controller.book;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.S5G.shop.dto.category.CategoryRequestDto;
import shop.S5G.shop.dto.category.CategoryResponseDto;
import shop.S5G.shop.entity.Category;
import shop.S5G.shop.exception.CategoryException.CategoryBadRequestException;
import shop.S5G.shop.service.category.impl.CategoryServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class CategoryController {
    private final CategoryServiceImpl categoryServiceImpl;
    public CategoryController(CategoryServiceImpl categoryServiceImpl) {
        this.categoryServiceImpl = categoryServiceImpl;
    }

    //카테고리 등록
    @PostMapping("/category")
    public ResponseEntity addCategory(@Valid @RequestBody CategoryRequestDto categorydto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new CategoryBadRequestException("잘못된 입력입니다.");
        }
        Category category = new Category(categorydto.getCategoryName(), categorydto.isActive());
        categoryServiceImpl.createCategory(categorydto);
        return ResponseEntity.ok().build();
    }

    //카테고리 목록 조회
    @GetMapping("/category")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categoryResponseDto = categoryServiceImpl.allCategory();
        return ResponseEntity.ok().body(categoryResponseDto);
    }

    //카테고리 수정
    @PutMapping("/category/{categoryId}")
    public ResponseEntity updateCategory(@Valid @PathVariable("categoryId") Long categoryId, @Valid @RequestBody CategoryRequestDto categoryDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new CategoryBadRequestException("잘못된 입력입니다.");
        }
        if (categoryId < 1) {
            throw new CategoryBadRequestException("categoryId must be greater than 0");
        }

        categoryServiceImpl.updateCategory(categoryId, categoryDto);
        return ResponseEntity.ok().build();
    }
    //카테고리 삭제
    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity deleteCategory(@PathVariable("categoryId") Long categoryId) {
        if (categoryId < 1) {
            throw new CategoryBadRequestException("categoryId must be greater than 0");
        }
        categoryServiceImpl.deleteCategory(categoryId);
        return ResponseEntity.ok().build();
    }
}
