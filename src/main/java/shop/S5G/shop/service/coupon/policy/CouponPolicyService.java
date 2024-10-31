package shop.S5G.shop.service.coupon.policy;

import java.util.List;
import shop.S5G.shop.dto.coupon.policy.CouponPolicyRequestDto;
import shop.S5G.shop.dto.coupon.policy.CouponPolicyResponseDto;
import shop.S5G.shop.entity.coupon.CouponPolicy;

public interface CouponPolicyService {

    CouponPolicy saveCouponPolicy(CouponPolicyRequestDto couponPolicyRequestDto);

    void updateCouponPolicy(Long couponPolicyId, CouponPolicyRequestDto couponPolicyRequestDto);

    CouponPolicyResponseDto findByCouponPolicyId(Long couponPolicyId);

    List<CouponPolicyResponseDto> findByAllCouponPolicies();

}
