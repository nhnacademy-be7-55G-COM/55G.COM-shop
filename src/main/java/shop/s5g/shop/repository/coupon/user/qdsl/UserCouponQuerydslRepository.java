package shop.s5g.shop.repository.coupon.user.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.coupon.user.InValidUsedCouponResponseDto;
import shop.s5g.shop.dto.coupon.user.ValidUserCouponResponseDto;

public interface UserCouponQuerydslRepository {

    // 해당 쿠폰 템플릿이 있는 지 조회
    boolean userHasCouponTemplate(Long memberId, String couponTemplateName);

    // 사용하지 않은 유효한 쿠폰 조회
    Page<ValidUserCouponResponseDto> findUnusedCouponList(Long customerId, Pageable pageable);

    // 사용자가 사용한 쿠폰 조회
    Page<InValidUsedCouponResponseDto> findUsedCouponList(Long customerId, Pageable pageable);

    // 사용자가 사용하진 않았으나 기간이 지난 쿠폰 조회
    Page<InValidUsedCouponResponseDto> findAfterExpiredUserCouponList(Long customerId, Pageable pageable);

    // 기간이 지났거나 사용한 쿠폰 조회
    Page<InValidUsedCouponResponseDto> findInvalidUserCouponList(Long customerId, Pageable pageable);

}
