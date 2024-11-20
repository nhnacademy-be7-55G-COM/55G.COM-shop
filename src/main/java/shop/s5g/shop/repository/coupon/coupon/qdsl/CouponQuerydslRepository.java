package shop.s5g.shop.repository.coupon.coupon.qdsl;

import java.time.LocalDateTime;
import shop.s5g.shop.dto.coupon.coupon.CouponResponseDto;

public interface CouponQuerydslRepository {
    // 쿠폰 만료일 업데이트
    void updateCouponExpiredDatetime(Long couponId, LocalDateTime expiredAt);

    // 특정 쿠폰 찾기
    CouponResponseDto findCoupon(Long couponId);

    // 쿠폰 삭제
    void deleteCouponById(Long couponId);

    // 활성화된 쿠폰인지 체크
    boolean checkActiveCoupon(Long couponId);


    // 쿠폰 조회 ( 기간으로 조회 )

    // 사용한 쿠폰 조회 ( 기간으로 조회 )
}
