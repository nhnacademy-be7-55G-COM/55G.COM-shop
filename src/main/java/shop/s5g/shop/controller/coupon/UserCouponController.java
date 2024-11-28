package shop.s5g.shop.controller.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.config.RedisConfig;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.coupon.user.InValidUsedCouponResponseDto;
import shop.s5g.shop.dto.coupon.user.ValidUserCouponResponseDto;
import shop.s5g.shop.service.coupon.user.UserCouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop")
@ConditionalOnBean(RedisConfig.class)
public class UserCouponController {

    private final UserCouponService userCouponService;

    /**
     * 해당 멤버의 사용 가능한 모든 쿠폰 조회
     * @param memberId
     * @param pageable
     * @return PageResponseDto<UserCouponResponseDto
     */
    @GetMapping("/member/coupons/{userId}")
    public ResponseEntity<PageResponseDto<ValidUserCouponResponseDto>> getUserCoupons(
        @PathVariable("userId") Long memberId, Pageable pageable) {

        Page<ValidUserCouponResponseDto> userCouponList = userCouponService.getUnusedCoupons(memberId, pageable);

        return ResponseEntity.status(HttpStatus.OK)
            .body(PageResponseDto.of(userCouponList));
    }

    /**
     * 해당 멤버가 사용한 모든 쿠폰 조회
     * @param memberId
     * @param pageable
     * @return PageResponseDto<InValidUsedCouponResponseDto>
     */
    @GetMapping("/member/coupons/used/{userId}")
    public ResponseEntity<PageResponseDto<InValidUsedCouponResponseDto>> getUserUsedCoupons(
        @PathVariable("userId") Long memberId, Pageable pageable) {

        Page<InValidUsedCouponResponseDto> usedCouponList = userCouponService.getUsedCoupons(memberId, pageable);

        return ResponseEntity.status(HttpStatus.OK)
            .body(PageResponseDto.of(usedCouponList));
    }

    /**
     * 해당 멤버가 사용하지 않았으나 사용기간이 만료된 쿠폰 조회
     * @param memberId
     * @param pageable
     * @return PageResponseDto<InValidUsedCouponResponseDto>
     */
    @GetMapping("/member/coupons/expired/{userId}")
    public ResponseEntity<PageResponseDto<InValidUsedCouponResponseDto>> getUserExpiredCoupons(
        @PathVariable("userId") Long memberId, Pageable pageable) {

        Page<InValidUsedCouponResponseDto> expiredCoupons = userCouponService.getExpiredCoupons(memberId, pageable);

        return ResponseEntity.status(HttpStatus.OK)
            .body(PageResponseDto.of(expiredCoupons));
    }

    /**
     * 해다 멤버의 사용기간이 만료됐거나, 사용한 쿠폰 조회
     * @param memberId
     * @param pageable
     * @return PageResponseDto<InValidUsedCouponResponseDto>
     */
    @GetMapping("/member/coupons/invalid/{userId}")
    public ResponseEntity<PageResponseDto<InValidUsedCouponResponseDto>> getUserInvalidCoupons(
        @PathVariable("userId") Long memberId, Pageable pageable) {

        Page<InValidUsedCouponResponseDto> invalidCoupons = userCouponService.getInValidCoupons(memberId, pageable);

        return ResponseEntity.status(HttpStatus.OK)
            .body(PageResponseDto.of(invalidCoupons));
    }
}
