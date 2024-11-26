package shop.s5g.shop.service.coupon.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.coupon.user.InValidUsedCouponResponseDto;
import shop.s5g.shop.dto.coupon.user.UserCouponRequestDto;
import shop.s5g.shop.dto.coupon.user.ValidUserCouponResponseDto;

public interface UserCouponService {

    void createWelcomeCoupon(UserCouponRequestDto userCouponRequestDto);

    void createCategoryCoupon(Long memberId, String categoryName);

    // 사용자가 사용하지 않은 유효한 쿠폰 조회
    Page<ValidUserCouponResponseDto> getUnusedCoupons(Long customerId, Pageable pageable);

    // 사용자가 사용한 쿠폰 조회
    Page<InValidUsedCouponResponseDto> getUsedCoupons(Long customerId, Pageable pageable);

    // 사용자가 사용하진 않았으나 기간이 지난 쿠폰 조회
    Page<InValidUsedCouponResponseDto> getExpiredCoupons(Long customerId, Pageable pageable);

    // 기간이 지났거나 사용한 쿠폰 조회
    Page<InValidUsedCouponResponseDto> getInValidCoupons(Long customerId, Pageable pageable);

    void addUserCoupon(Long customerId, Long couponTemplateId);
}
