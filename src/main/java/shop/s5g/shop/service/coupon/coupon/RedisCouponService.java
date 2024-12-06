package shop.s5g.shop.service.coupon.coupon;

import shop.s5g.shop.dto.coupon.coupon.CouponRequestDto;

public interface RedisCouponService {
    void createCoupon(CouponRequestDto couponRequestDto);
}
