package shop.s5g.shop.repository.coupon.couponpolicy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import shop.s5g.shop.config.TestQueryFactoryConfig;
import shop.s5g.shop.dto.coupon.policy.CouponPolicyResponseDto;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.repository.coupon.policy.CouponPolicyRepository;

@DataJpaTest
@ActiveProfiles("local")
@Import(TestQueryFactoryConfig.class)
class CouponPolicyRepositoryTest {

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;

    @Test
    @DisplayName("쿠폰 정책 생성 - save")
    void save() {
        CouponPolicy couponPolicy = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.5"))
            .condition(20000L)
            .maxPrice(2000L)
            .duration(30)
            .build();

        CouponPolicy saveCouponPolicy = couponPolicyRepository.save(couponPolicy);

        Assertions.assertEquals(new BigDecimal("0.5"), saveCouponPolicy.getDiscountPrice());
        Assertions.assertEquals(20000L, saveCouponPolicy.getCondition());
        Assertions.assertEquals(2000L, saveCouponPolicy.getMaxPrice());
        Assertions.assertEquals(30, saveCouponPolicy.getDuration());
    }

    @Test
    @DisplayName("쿠폰 정책 수정 - update")
    void update() {
        CouponPolicy couponPolicy = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.5"))
            .condition(20000L)
            .maxPrice(2000L)
            .duration(30)
            .build();

        CouponPolicy saveCouponPolicy = couponPolicyRepository.save(couponPolicy);
        saveCouponPolicy.setCondition(50000L);
        saveCouponPolicy.setDiscountPrice(new BigDecimal("0.1"));

         couponPolicyRepository.updateCouponPolicy(
            saveCouponPolicy.getCouponPolicyId(),
            saveCouponPolicy.getDiscountPrice(),
            saveCouponPolicy.getCondition(),
            saveCouponPolicy.getMaxPrice(),
            saveCouponPolicy.getDuration());

         CouponPolicy findCouponPolicy = couponPolicyRepository.findById(saveCouponPolicy.getCouponPolicyId()).get();

         Assertions.assertEquals(50000L, findCouponPolicy.getCondition());
         Assertions.assertEquals(new BigDecimal("0.1"), findCouponPolicy.getDiscountPrice());
    }

    @Test
    @DisplayName("특정 쿠폰 정책 조회 - findById")
    void findById() {
        CouponPolicy couponPolicy = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.5"))
            .condition(20000L)
            .maxPrice(2000L)
            .duration(30)
            .build();

        CouponPolicy saveCouponPolicy = couponPolicyRepository.save(couponPolicy);

        CouponPolicy findCouponPolicy = couponPolicyRepository.findById(saveCouponPolicy.getCouponPolicyId()).get();

        Assertions.assertEquals(findCouponPolicy.getDiscountPrice(), saveCouponPolicy.getDiscountPrice());
        Assertions.assertEquals(findCouponPolicy.getCondition(), saveCouponPolicy.getCondition());
        Assertions.assertEquals(findCouponPolicy.getMaxPrice(), saveCouponPolicy.getMaxPrice());
        Assertions.assertEquals(findCouponPolicy.getDuration(), saveCouponPolicy.getDuration());
    }

    @Test
    @DisplayName("모든 쿠폰 정책 조회 테스트 - findAll")
    void findAll() {
        CouponPolicy couponPolicy1 = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.5"))
            .condition(20000L)
            .maxPrice(2000L)
            .duration(30)
            .build();

        CouponPolicy couponPolicy2 = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.5"))
            .condition(20000L)
            .maxPrice(2000L)
            .duration(30)
            .build();

        couponPolicyRepository.save(couponPolicy1);
        couponPolicyRepository.save(couponPolicy2);

        Assertions.assertEquals(2, couponPolicyRepository.count());
    }

    @Test
    @DisplayName("쿠폰 정책 조회")
    void findAllCouponPolicies() {

        CouponPolicy couponPolicy1 = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.5"))
            .condition(20000L)
            .maxPrice(2000L)
            .duration(30)
            .build();

        CouponPolicy couponPolicy2 = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.5"))
            .condition(20000L)
            .maxPrice(2000L)
            .duration(30)
            .build();

        couponPolicyRepository.save(couponPolicy1);
        couponPolicyRepository.save(couponPolicy2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<CouponPolicyResponseDto> couponPolicyList = couponPolicyRepository.findAllCouponPolicies(pageable);

        assertNotNull(couponPolicyList);
        assertEquals(2, couponPolicyList.getTotalElements());
        assertEquals(1, couponPolicyList.getTotalPages());
        assertEquals(2, couponPolicyList.getContent().size());

        CouponPolicyResponseDto couponPolicy = couponPolicyList.getContent().get(0);
        assertEquals(new BigDecimal("0.50"), couponPolicy.discountPrice());
        assertEquals(20000L, couponPolicy.condition());
        assertEquals(2000L, couponPolicy.maxPrice());
    }
}
