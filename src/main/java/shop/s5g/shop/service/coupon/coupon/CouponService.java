package shop.s5g.shop.service.coupon.coupon;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.coupon.coupon.CouponRequestDto;
import shop.s5g.shop.dto.coupon.coupon.CouponResponseDto;

public interface CouponService {

    void createCoupon(Integer couponCnt, CouponRequestDto couponRequestDto);

    CouponResponseDto getCoupon(Long couponId);

    List<CouponResponseDto> getCoupons(Pageable pageable);

    void updateCoupon(Long couponId, LocalDateTime expiredAt);

    void deleteCoupon(Long couponId);
}
