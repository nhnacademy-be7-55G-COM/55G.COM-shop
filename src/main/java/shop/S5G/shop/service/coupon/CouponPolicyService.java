package shop.S5G.shop.service.coupon;

import java.util.List;
import shop.S5G.shop.dto.couponpolicy.CouponPolicyRequestDto;
import shop.S5G.shop.dto.couponpolicy.CouponPolicyResponseDto;

public interface CouponPolicyService {

    void saveCouponPolicy(CouponPolicyRequestDto couponPolicyRequestDto);

    void updateCouponPolicy(Long couponPolicyId, CouponPolicyRequestDto couponPolicyRequestDto);

    CouponPolicyResponseDto findByCouponPolicyId(Long couponPolicyId);

    List<CouponPolicyResponseDto> findByAllCouponPolicies();

}
