package shop.S5G.shop.entity;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import shop.S5G.shop.entity.coupon.CouponPolicy;

public class CouponPolicyEntityBuilderTest {

    @Test
    void createCouponPolicyWithBuilder() {
        // Given
        BigDecimal discountPrice = BigDecimal.valueOf(100);
        Long condition = 500L;
        Long maxPrice = 1000L;
        Integer duration = 30;

        // When
        CouponPolicy couponPolicy = CouponPolicy.builder()
            .discountPrice(discountPrice)
            .condition(condition)
            .maxPrice(maxPrice)
            .duration(duration)
            .build();

        // Then
        assertThat(couponPolicy).isNotNull();
        assertThat(couponPolicy.getDiscountPrice()).isEqualTo(discountPrice);
        assertThat(couponPolicy.getCondition()).isEqualTo(condition);
        assertThat(couponPolicy.getMaxPrice()).isEqualTo(maxPrice);
        assertThat(couponPolicy.getDuration()).isEqualTo(duration);
    }
}
