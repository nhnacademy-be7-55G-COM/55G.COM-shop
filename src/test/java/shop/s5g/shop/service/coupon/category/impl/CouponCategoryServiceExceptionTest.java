package shop.s5g.shop.service.coupon.category.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import shop.s5g.shop.dto.coupon.category.CouponCategoryRequestDto;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.exception.category.CategoryResourceNotFoundException;
import shop.s5g.shop.exception.coupon.CouponCategoryAlreadyExistsException;
import shop.s5g.shop.exception.coupon.CouponTemplateNotFoundException;
import shop.s5g.shop.repository.category.CategoryRepository;
import shop.s5g.shop.repository.coupon.category.CouponCategoryRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;

@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
class CouponCategoryServiceExceptionTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CouponTemplateRepository couponTemplateRepository;
    @Mock
    private CouponCategoryRepository couponCategoryRepository;
    @InjectMocks
    private CouponCategoryServiceImpl couponCategoryService;

    @Test
    @DisplayName("카테고리가 존재하지 않으면 예외 발생")
    void createCouponCategoryException() {
        // Given
        Long categoryId = 1L;
        Long couponTemplateId = 1L;
        CouponCategoryRequestDto couponCategoryRequestDto = new CouponCategoryRequestDto(
            couponTemplateId, categoryId);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When & Then
        CategoryResourceNotFoundException exception = assertThrows(
            CategoryResourceNotFoundException.class,
            () -> couponCategoryService.createCouponCategory(couponCategoryRequestDto)
        );

        assertEquals("해당 카테고리를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("쿠폰 템플릿이 존재하지 않으면 예외 발생")
    void createCouponCategory_couponTemplateNotFound() {
        // Given
        Long categoryId = 1L;
        Long couponTemplateId = 1L;
        CouponCategoryRequestDto couponCategoryRequestDto = new CouponCategoryRequestDto(
            couponTemplateId, categoryId);

        Category category = new Category(
            null,
            "테스트",
            true
        );

        // 카테고리는 존재하지만 쿠폰 템플릿이 존재하지 않으면 예외 발생
        when(categoryRepository.findById(categoryId))
            .thenReturn(Optional.of(category));
        when(couponTemplateRepository.findById(couponTemplateId))
            .thenReturn(Optional.empty());

        // When & Then
        CouponTemplateNotFoundException exception = assertThrows(
            CouponTemplateNotFoundException.class,
            () -> couponCategoryService.createCouponCategory(couponCategoryRequestDto));

        assertEquals("해당 쿠폰 템플릿이 존재하지 않습니다.", exception.getMessage());
    }

    @Test
    @DisplayName("이미 존재하는 카테고리 쿠폰일 경우 예외 발생")
    void createCouponCategory_alreadyExists() {
        // Given
        Long categoryId = 1L;
        Long couponTemplateId = 1L;
        CouponCategoryRequestDto couponCategoryRequestDto = new CouponCategoryRequestDto(
            couponTemplateId, categoryId);

        Category category = new Category(
            null,
            "테스트",
            true
        );

        CouponPolicy couponPolicy = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.5"))
            .condition(20000L)
            .maxPrice(2000L)
            .duration(30)
            .build();

        CouponTemplate couponTemplate = new CouponTemplate(
            couponPolicy,
            "생일쿠폰",
            "이 달의 생일자를 위한 할인 쿠폰입니다."
        );

        when(categoryRepository.findById(categoryId))
            .thenReturn(Optional.of(category));
        when(couponTemplateRepository.findById(couponTemplateId))
            .thenReturn(Optional.of(couponTemplate));
        when(couponCategoryRepository.existsByCouponTemplateAndCategory(couponTemplateId, categoryId))
            .thenReturn(true);

        // When & Then
        CouponCategoryAlreadyExistsException exception = assertThrows(
            CouponCategoryAlreadyExistsException.class,
            () -> couponCategoryService.createCouponCategory(couponCategoryRequestDto));

        assertEquals("이미 존재하는 카테고리 쿠폰입니다.", exception.getMessage());
    }
}
