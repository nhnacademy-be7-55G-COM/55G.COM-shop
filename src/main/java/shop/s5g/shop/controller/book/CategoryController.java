package shop.s5g.shop.controller.book;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.category.CategoryDetailResponseDto;
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
    public ResponseEntity<PageResponseDto<CategoryResponseDto>> getAllCategories(Pageable pageable) {
        Page<CategoryResponseDto> categoryResponseDto = categoryServiceImpl.allCategory(pageable);
        return ResponseEntity.ok().body(PageResponseDto.of(categoryResponseDto));
    }

    //자식 카테고리 조회
    @GetMapping("/category/childCategory/{categoryId}")
    public ResponseEntity<List<CategoryResponseDto>> getChildCategories(@Valid @PathVariable("categoryId") long categoryId) {
        return ResponseEntity.ok().body(categoryServiceImpl.getChildCategory(categoryId));
    }

    //국내도서 하위 카테고리 조회
    @GetMapping("/category/korea")
    public ResponseEntity<List<CategoryResponseDto>> getKoreaCategories() {
        return ResponseEntity.ok().body(categoryServiceImpl.getKoreaBooks());
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

    /**
     * 카테고리 ID로 조회 - API
     * @param categoryId
     * @return ResponseEntity<CategoryResponseDto>
     */
    @GetMapping("/admin/coupons/category/{categoryId}")
    public ResponseEntity<CategoryResponseDto> findCategoryById(@PathVariable("categoryId") Long categoryId) {
        CategoryResponseDto category = categoryServiceImpl.getCategory(categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(category);
    }

    //카테고리 수정

}
