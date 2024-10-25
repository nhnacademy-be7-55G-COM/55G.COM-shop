package shop.S5G.shop.repository.coupon.qdsl;

import java.math.BigDecimal;

public interface CouponPolicyQuerydslRepository {
    void updateCouponPolicy(Long couponId, BigDecimal discountPrice, Long condition, Long maxPrice, Integer duration);
}
