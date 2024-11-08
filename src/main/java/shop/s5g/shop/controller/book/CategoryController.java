package shop.s5g.shop.controller.book;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.s5g.shop.dto.category.CategoryRequestDto;
import shop.s5g.shop.dto.category.CategoryResponseDto;
import shop.s5g.shop.dto.category.CategoryUpdateRequestDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.category.CategoryBadRequestException;
import shop.s5g.shop.service.category.impl.CategoryServiceImpl;

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
    public ResponseEntity<MessageDto> addCategory(@Valid @RequestBody CategoryRequestDto categoryDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new CategoryBadRequestException("잘못된 입력입니다.");
        }
        categoryServiceImpl.createCategory(categoryDto);
        return ResponseEntity.ok().body(new MessageDto("카테고리 등록 성공"));
    }

    //카테고리 목록 조회
    @GetMapping("/category")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        List<CategoryResponseDto> categoryResponseDto = categoryServiceImpl.allCategory();
        return ResponseEntity.ok().body(categoryResponseDto);
    }

    //카테고리 수정
    @PutMapping("/category/{categoryId}")
    public ResponseEntity<MessageDto> updateCategory(@Valid @PathVariable("categoryId") Long categoryId, @Valid @RequestBody CategoryUpdateRequestDto categoryDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new CategoryBadRequestException("잘못된 입력입니다.");
        }
        if (categoryId < 1) {
            throw new CategoryBadRequestException("카테고리 Id는 1보다 커야 합니다.");
        }

        categoryServiceImpl.updateCategory(categoryId, categoryDto);
        return ResponseEntity.ok().body(new MessageDto("카테고리 수정 성공"));
    }

    //카테고리 삭제
    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<MessageDto> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        if (categoryId < 1) {
            throw new CategoryBadRequestException("카테고리 Id는 1보다 커야 합니다.");
        }
        categoryServiceImpl.deleteCategory(categoryId);
        return ResponseEntity.ok().body(new MessageDto("카테고리 삭제 성공"));
    }
}
