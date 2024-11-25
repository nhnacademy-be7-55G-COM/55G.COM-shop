package shop.s5g.shop.repository.coupon.coupon.qdsl;

import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // 발급된 쿠폰 리스트 가져오기
    Page<CouponResponseDto> getAllIssuedCoupons(Pageable pageable);

    // 만료일 지난 쿠폰 비활성화 해주기
    void deactivateExpiredCoupons();

    // 해당 쿠폰이 만료일이 지났는 지 확인하기
    boolean checkIfCouponExpired(Long couponId);
}
