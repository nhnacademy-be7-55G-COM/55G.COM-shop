package shop.S5G.shop.repository.coupon.coupon.qdsl;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import shop.S5G.shop.dto.coupon.coupon.CouponRequestDto;
import shop.S5G.shop.dto.coupon.coupon.CouponResponseDto;

public interface CouponQuerydslRepository {

    CouponResponseDto createCoupon(CouponRequestDto couponRequestDto);

    void updateCouponExpiredDatetime(Long couponId, LocalDateTime expiredAt);

    CouponResponseDto findCoupon(Long couponId);

    List<CouponResponseDto> findCoupons(Pageable pageable);

    void deleteCoupon(Long couponId);
}
