package shop.s5g.shop.repository.coupon.coupon;

import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import shop.s5g.shop.config.TestQueryFactoryConfig;
import shop.s5g.shop.dto.coupon.coupon.AvailableCouponResponseDto;
import shop.s5g.shop.dto.coupon.coupon.CouponResponseDto;
import shop.s5g.shop.entity.coupon.Coupon;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.repository.coupon.policy.CouponPolicyRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;

@DataJpaTest
@ActiveProfiles("local")
@Import(TestQueryFactoryConfig.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class CouponRepositoryTest {

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;
    @Autowired
    private CouponTemplateRepository couponTemplateRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private EntityManager entityManager;

    private Coupon coupon;
    private Coupon saved;

    @BeforeEach
    void setUp() {
        CouponPolicy couponPolicy = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.5"))
            .condition(20000L)
            .maxPrice(2000L)
            .duration(30)
            .build();

        couponPolicyRepository.save(couponPolicy);

        CouponTemplate couponTemplate = new CouponTemplate(
            couponPolicy,
            "Welcome Coupon",
            "Welcome to my Page"
        );

        couponTemplateRepository.save(couponTemplate);

        coupon = new Coupon(
            couponTemplate,
            "xXzZcCdDfFgG"
        );

        saved = couponRepository.save(coupon);
    }

    @Test
    @Order(1)
    @DisplayName("쿠폰 찾기")
    void findCoupon() {

        CouponResponseDto findCoupon = couponRepository.findCoupon(saved.getCouponId());

        Assertions.assertNotNull(findCoupon);
        Assertions.assertEquals(coupon.getCouponCode(), findCoupon.couponCode());
        Assertions.assertEquals(coupon.getCouponTemplate().getCouponTemplateId(), findCoupon.couponTemplateId());
    }

    @Test
    @Order(5)
    @DisplayName("쿠폰 삭제")
    void deleteCoupon() {

        couponRepository.deleteCouponById(saved.getCouponId());

        entityManager.flush();
        entityManager.clear();

        Coupon updatedCoupon = couponRepository.findById(saved.getCouponId()).orElseThrow();

        Assertions.assertFalse(updatedCoupon.isActive());
    }

    @Test
    @Order(2)
    @DisplayName("활성화중인지 체크하기")
    void checkActiveCoupon() {

        boolean checkCoupon = couponRepository.checkActiveCoupon(saved.getCouponId());

        Assertions.assertTrue(checkCoupon);
    }

    @Test
    @Order(3)
    @DisplayName("모든 쿠폰 가져오기")
    void allIssuedCoupons() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<CouponResponseDto> couponList = couponRepository.getAllIssuedCoupons(pageable);

        Assertions.assertNotNull(couponList);
        Assertions.assertEquals(1, couponList.getContent().size());
    }

    @Test
    @Order(4)
    @DisplayName("발급 가능한 쿠폰 가져오기")
    void allAvailableCoupons() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<AvailableCouponResponseDto> couponList = couponRepository.getAllAvailableCoupons(pageable);

        Assertions.assertNotNull(couponList);
        Assertions.assertEquals(0, couponList.getContent().size());
    }
}
