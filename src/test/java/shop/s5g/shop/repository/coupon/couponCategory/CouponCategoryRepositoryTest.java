package shop.S5G.shop.repository.coupon.couponCategory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import shop.s5g.shop.config.QueryFactoryConfig;
import shop.s5g.shop.dto.coupon.category.CouponCategoryDetailsForCategoryDto;
import shop.s5g.shop.dto.coupon.category.CouponCategoryResponseDto;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.entity.coupon.CouponCategory;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.repository.category.CategoryRepository;
import shop.s5g.shop.repository.coupon.category.CouponCategoryRepository;
import shop.s5g.shop.repository.coupon.policy.CouponPolicyRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;

@DataJpaTest
@ActiveProfiles("local")
@Import(QueryFactoryConfig.class)
class CouponCategoryRepositoryTest {

    @Autowired
    private CouponTemplateRepository couponTemplateRepository;
    @Autowired
    private CouponPolicyRepository couponPolicyRepository;
    @Autowired
    private CouponCategoryRepository couponCategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private CouponTemplate couponTemplate;
    private CouponPolicy couponPolicy;
    private Category category;
    private CouponCategory couponCategory;

    @BeforeEach
    void setUp() {
        couponPolicy = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.5"))
            .condition(20000L)
            .maxPrice(2000L)
            .duration(30)
            .build();
        // 정책 저장
        couponPolicyRepository.save(couponPolicy);

        couponTemplate = new CouponTemplate(
            couponPolicy,
            "카테고리 쿠폰",
            "카테고리 테스트 중"
        );
        // 템플릿 저장
        couponTemplateRepository.save(couponTemplate);

        category = new Category(
            null,
            "테스트",
            true
        );
        // 카테고리 저장
        categoryRepository.save(category);

        couponCategory = new CouponCategory(
            couponTemplate,
            category
        );
        // 카테고리 쿠폰 저장
        couponCategoryRepository.save(couponCategory);
    }

    @Test
    @DisplayName("카테고리 쿠폰이 존재하는 지 체크")
    void existsCouponCategory() {

        boolean exists = couponCategoryRepository.existsByCouponTemplateAndCategory(couponTemplate.getCouponTemplateId(), category.getCategoryId());
        boolean notExists = couponCategoryRepository.existsByCouponTemplateAndCategory(99L, 99L);


        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    @DisplayName("모든 카테고리 쿠폰 조회")
    void findAllCouponCategory() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<CouponCategoryResponseDto> couponCategoryList = couponCategoryRepository.findAllCouponCategories(pageable);

        assertNotNull(couponCategoryList);
        assertEquals(1, couponCategoryList.getTotalElements());
        assertEquals(1, couponCategoryList.getTotalPages());
        assertEquals(1, couponCategoryList.getContent().size());
    }

    @Test
    @DisplayName("쿠폰 적용이 된 모든 카테고리 조회")
    void findAllCouponCategoryByCouponTemplate() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<CouponCategoryDetailsForCategoryDto> categoryList = couponCategoryRepository.findCategoryByCouponTemplateId(couponTemplate.getCouponTemplateId(), pageable);

        assertNotNull(categoryList);
        assertEquals(1, categoryList.getTotalElements());
        assertEquals(1, categoryList.getTotalPages());
        assertEquals(1, categoryList.getContent().size());

        CouponCategoryDetailsForCategoryDto categoryDto = categoryList.getContent().get(0);

        assertNotNull(categoryDto);
        assertEquals("테스트", categoryDto.categoryName());
    }
}
