package shop.s5g.shop.service.coupon.coupon;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.coupon.coupon.AvailableCouponResponseDto;
import shop.s5g.shop.dto.coupon.coupon.CouponRequestDto;
import shop.s5g.shop.dto.coupon.coupon.CouponResponseDto;
import shop.s5g.shop.entity.coupon.Coupon;

public interface CouponService {

    // 쿠폰 생성
    Coupon createWelcomeCoupon();

    // 쿠폰 가져오기
    Coupon getCouponByCode(String couponCode);
    CouponResponseDto getCoupon(Long couponId);
    Page<CouponResponseDto> getAllCouponList(Pageable pageable);

    void deleteCoupon(Long couponId);

    Page<AvailableCouponResponseDto> getAvailableCoupons(Pageable pageable);

}
