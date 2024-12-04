package shop.s5g.shop.repository.coupon.policy.qdsl;

import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.coupon.policy.CouponPolicyResponseDto;

public interface CouponPolicyQuerydslRepository {

    void updateCouponPolicy(Long couponId, BigDecimal discountPrice, Long condition, Long maxPrice, Integer duration);

    Page<CouponPolicyResponseDto> findAllCouponPolicies(Pageable pageable);
}
