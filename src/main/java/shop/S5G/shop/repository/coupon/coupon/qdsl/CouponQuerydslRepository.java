package shop.S5G.shop.repository.coupon.coupon.qdsl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import shop.S5G.shop.dto.coupon.coupon.CouponResponseDto;

public interface CouponQuerydslRepository {

    void updateCouponExpiredDatetime(Long couponId, LocalDateTime expiredAt);

    CouponResponseDto findCoupon(Long couponId);

    List<CouponResponseDto> findCoupons(Pageable pageable);

    void deleteCouponById(Long couponId);
}
