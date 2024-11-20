package shop.s5g.shop.service.coupon.user;

import shop.s5g.shop.dto.coupon.user.UserCouponRequestDto;

public interface UserCouponService {

    void createWelcomeCoupon(UserCouponRequestDto userCouponRequestDto);

    void createBirthCoupon(UserCouponRequestDto userCouponRequestDto);
}
