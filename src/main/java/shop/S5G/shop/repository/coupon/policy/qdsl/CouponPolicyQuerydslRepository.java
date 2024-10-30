package shop.S5G.shop.repository.coupon.policy.qdsl;

import java.math.BigDecimal;
import java.util.List;
import shop.S5G.shop.dto.coupon.policy.CouponPolicyResponseDto;

public interface CouponPolicyQuerydslRepository {

    void updateCouponPolicy(Long couponId, BigDecimal discountPrice, Long condition, Long maxPrice, Integer duration);

    List<CouponPolicyResponseDto> findAllCouponPolicies();
}
