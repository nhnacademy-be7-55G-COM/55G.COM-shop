package shop.s5g.shop.controller.coupon;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.category.CategoryResponseDto;
import shop.s5g.shop.dto.coupon.category.CouponCategoryDetailsForCategoryDto;
import shop.s5g.shop.dto.coupon.category.CouponCategoryRequestDto;
import shop.s5g.shop.dto.coupon.category.CouponCategoryResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.service.category.CategoryService;
import shop.s5g.shop.service.coupon.category.CouponCategoryService;

/**
 * 카테고리 쿠폰 컨트롤러
 * Author: DooYoungHo
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/admin/coupons")
public class CouponCategoryController {

    private final CouponCategoryService couponCategoryService;
    private final CategoryService categoryService;

    /**
     * 카테고리 쿠폰 생성 API
     * @param couponCategoryRequestDto
     * @param result
     * @return ResponseEntity<MessageDto>
     */
    @PostMapping("/category")
    public ResponseEntity<MessageDto> createCouponCategory(
        @Valid @RequestBody CouponCategoryRequestDto couponCategoryRequestDto,
        BindingResult result) {

        if (result.hasErrors()) {
            throw new BadRequestException("잘못된 요청입니다.");
        }

        couponCategoryService.createCouponCategory(couponCategoryRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDto("카테고리 쿠폰 생성 성공"));
    }

    /**
     * 모든 쿠폰 카테고리 조회 - API
     * @param pageable
     * @return ResponseEntity<PageResponseDto<CouponCategoryResponseDto>>
     */
    @GetMapping("/categories")
    public ResponseEntity<PageResponseDto<CouponCategoryResponseDto>> getCouponCategories(Pageable pageable) {
       Page<CouponCategoryResponseDto> couponCategories = couponCategoryService.getAllCouponCategories(pageable);

       return ResponseEntity.status(HttpStatus.OK).body(PageResponseDto.of(couponCategories));
    }

    /**
     * 쿠폰이 적용된 카테고리 조회 - API
     * @param pageable
     * @return ResponseEntity<PageResponseDto<CouponCategoryDetailsForCategoryDto>>
     */
    @GetMapping("/categories/name/templates")
    public ResponseEntity<PageResponseDto<CouponCategoryDetailsForCategoryDto>> getCategoryName(Pageable pageable) {
        Page<CouponCategoryDetailsForCategoryDto> categoryNames = couponCategoryService.getCategoriesByCouponTemplate(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(PageResponseDto.of(categoryNames));
    }

    /**
     * 카테고리 ID로 조회 - API
     * @param categoryId
     * @return ResponseEntity<CategoryResponseDto>
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<CategoryResponseDto> findCategoryById(@PathVariable("categoryId") Long categoryId) {
        CategoryResponseDto category = categoryService.getCategory(categoryId);

        return ResponseEntity.status(HttpStatus.OK).body(category);
    }
}
