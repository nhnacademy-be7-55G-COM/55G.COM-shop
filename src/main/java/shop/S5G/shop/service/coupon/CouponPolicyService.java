package shop.S5G.shop.service.coupon;

import java.util.List;
import shop.S5G.shop.dto.couponpolicy.CouponPolicyRequestDto;
import shop.S5G.shop.dto.couponpolicy.CouponPolicyResponseDto;
import shop.S5G.shop.entity.coupon.CouponPolicy;

public interface CouponPolicyService {

    CouponPolicy saveCouponPolicy(CouponPolicyRequestDto couponPolicyRequestDto);

    void updateCouponPolicy(Long couponPolicyId, CouponPolicyRequestDto couponPolicyRequestDto);

    CouponPolicyResponseDto findByCouponPolicyId(Long couponPolicyId);

    List<CouponPolicyResponseDto> findByAllCouponPolicies();

}
