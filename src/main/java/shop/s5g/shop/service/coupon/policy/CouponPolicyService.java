package shop.s5g.shop.service.coupon.policy;

import java.util.List;
import shop.s5g.shop.dto.coupon.policy.CouponPolicyRequestDto;
import shop.s5g.shop.dto.coupon.policy.CouponPolicyResponseDto;
import shop.s5g.shop.entity.coupon.CouponPolicy;

public interface CouponPolicyService {

    CouponPolicy saveCouponPolicy(CouponPolicyRequestDto couponPolicyRequestDto);

    void updateCouponPolicy(Long couponPolicyId, CouponPolicyRequestDto couponPolicyRequestDto);

    CouponPolicyResponseDto getByCouponPolicyId(Long couponPolicyId);

    List<CouponPolicyResponseDto> getAllCouponPolices();

}
