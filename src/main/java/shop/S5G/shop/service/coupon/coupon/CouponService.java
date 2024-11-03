package shop.S5G.shop.service.coupon.coupon;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import shop.S5G.shop.dto.coupon.coupon.CouponRequestDto;
import shop.S5G.shop.dto.coupon.coupon.CouponResponseDto;

public interface CouponService {

    void createCoupon(Integer couponCnt, CouponRequestDto couponRequestDto);

    CouponResponseDto findCoupon(Long couponId);

    List<CouponResponseDto> findCoupons(Pageable pageable);

    void updateCoupon(Long couponId, LocalDateTime expiredAt);

    void deleteCoupon(Long couponId);
}
