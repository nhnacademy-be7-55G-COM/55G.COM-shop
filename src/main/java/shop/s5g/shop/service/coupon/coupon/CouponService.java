package shop.s5g.shop.service.coupon.coupon;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.coupon.coupon.CouponRequestDto;
import shop.s5g.shop.dto.coupon.coupon.CouponResponseDto;
import shop.s5g.shop.entity.coupon.Coupon;

public interface CouponService {

    // 쿠폰 생성
    void createCoupon(Integer couponCnt, CouponRequestDto couponRequestDto);
    Coupon createWelcomeCoupon();
    Coupon createBirthCoupon();
    Coupon createCategoryCoupon();

    // 쿠폰 가져오기
    CouponResponseDto getCoupon(Long couponId);
    Page<CouponResponseDto> getAllCouponList(Pageable pageable);

    void updateCoupon(Long couponId, LocalDateTime expiredAt);

    void deleteCoupon(Long couponId);

}
