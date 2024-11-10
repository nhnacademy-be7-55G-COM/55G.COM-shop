package shop.s5g.shop.repository.coupon.coupon.qdsl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.coupon.coupon.CouponResponseDto;

public interface CouponQuerydslRepository {

    void updateCouponExpiredDatetime(Long couponId, LocalDateTime expiredAt);

    CouponResponseDto findCoupon(Long couponId);

    List<CouponResponseDto> findCoupons(Pageable pageable);

    void deleteCouponById(Long couponId);

    boolean checkActiveCoupon(Long couponId);
}
