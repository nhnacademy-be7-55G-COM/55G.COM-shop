package shop.s5g.shop.service.coupon.category.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import shop.s5g.shop.dto.coupon.category.CouponCategoryDetailsForCategoryDto;
import shop.s5g.shop.dto.coupon.category.CouponCategoryRequestDto;
import shop.s5g.shop.dto.coupon.category.CouponCategoryResponseDto;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.entity.coupon.CouponCategory;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.repository.category.CategoryRepository;
import shop.s5g.shop.repository.coupon.category.CouponCategoryRepository;
import shop.s5g.shop.repository.coupon.policy.CouponPolicyRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;

@ActiveProfiles("local")
@ExtendWith(MockitoExtension.class)
class CouponCategoryServiceTest {

    @Mock
    private CouponCategoryRepository couponCategoryRepository;
    @Mock
    private CouponPolicyRepository couponPolicyRepository;
    @Mock
    private CouponTemplateRepository couponTemplateRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CouponCategoryServiceImpl couponCategoryService;

    private CouponPolicy couponPolicy;
    private CouponTemplate couponTemplate;
    private Category category;

    @BeforeEach
    void setUp() {
        couponPolicy = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.5"))
            .condition(20000L)
            .maxPrice(2000L)
            .duration(30)
            .build();

        couponPolicyRepository.save(couponPolicy);

        couponTemplate = new CouponTemplate(
            couponPolicy,
            "생일쿠폰",
            "이 달의 생일자를 위한 할인 쿠폰입니다."
        );

        couponTemplateRepository.save(couponTemplate);

        category = new Category(
            null,
            "테스트",
            true
        );
    }

    @Test
    @DisplayName("쿠폰 카테고리 생성")
    void createCouponCategory() {
        // Given
        Long categoryId = 1L;
        Long couponTemplateId = 1L;
        CouponCategoryRequestDto couponCategoryRequestDto = new CouponCategoryRequestDto(
            couponTemplateId, categoryId);

        when(categoryRepository.findById(categoryId))
            .thenReturn(Optional.of(category));
        when(couponTemplateRepository.findById(couponTemplateId))
            .thenReturn(Optional.of(couponTemplate));

        // 이미 존재하는 카테고리 쿠폰이 아니라고 설정
        when(couponCategoryRepository.existsByCouponTemplateAndCategory(couponTemplateId, categoryId))
            .thenReturn(false);

        // When
        couponCategoryService.createCouponCategory(couponCategoryRequestDto);

        // Then
        verify(couponCategoryRepository, times(1)).save(any(CouponCategory.class));
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(couponTemplateRepository, times(1)).findById(couponTemplateId);
    }

    @Test
    @DisplayName("모든 쿠폰 카테고리 조회")
    void getAllCouponCategoriesTest() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        CouponCategoryResponseDto couponCategoryResponseDto = new CouponCategoryResponseDto(
            1L, "카테고리1", new BigDecimal("0.5"), 2000L, 5000L, 30, "쿠폰1", "설명1");

        List<CouponCategoryResponseDto> couponCategoryList = Arrays.asList(couponCategoryResponseDto);

        when(couponCategoryRepository.findAllCouponCategories(pageable))
            .thenReturn(new PageImpl<>(couponCategoryList, pageable, couponCategoryList.size()));

        // When
        Page<CouponCategoryResponseDto> result = couponCategoryService.getAllCouponCategories(pageable);

        // Then
        assertNotNull(result);
        verify(couponCategoryRepository, times(1)).findAllCouponCategories(pageable);
    }

    @Test
    @DisplayName("특정 쿠폰 템플릿 ID에 대한 카테고리 조회")
    void getCategoriesByCouponTemplateTest() {
        // Given
        Long couponTemplateId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        CouponCategoryDetailsForCategoryDto categoryDto = new CouponCategoryDetailsForCategoryDto(1L, "카테고리1");
        List<CouponCategoryDetailsForCategoryDto> categoryNames = Arrays.asList(categoryDto);

        when(couponCategoryRepository.findCategoryInfoByCouponTemplate(pageable))
            .thenReturn(new PageImpl<>(categoryNames, pageable, categoryNames.size()));

        // When
        Page<CouponCategoryDetailsForCategoryDto> result = couponCategoryService.getCategoriesByCouponTemplate(pageable);

        // Then
        assertNotNull(result);
        verify(couponCategoryRepository, times(1)).findCategoryInfoByCouponTemplate(pageable);
    }
}
