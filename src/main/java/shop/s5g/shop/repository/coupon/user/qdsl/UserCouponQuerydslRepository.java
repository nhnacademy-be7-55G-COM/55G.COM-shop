package shop.s5g.shop.repository.coupon.user.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.coupon.user.UserCouponResponseDto;

public interface UserCouponQuerydslRepository {
    // 해당 쿠폰 템플릿이 있는 지 조회
    boolean userHasCouponTemplate(Long memberId, String couponTemplateName);

    // 사용하지 않은 유효한 쿠폰 조회
    Page<UserCouponResponseDto> findUnusedCouponList(Long customerId, Pageable pageable);

    // 사용한 쿠폰 조회
    Page<UserCouponResponseDto> findUsedCouponList(Long customerId, Pageable pageable);
}
