package shop.s5g.shop.controller.coupon;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.config.RedisConfig;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.coupon.coupon.AvailableCouponResponseDto;
import shop.s5g.shop.dto.coupon.coupon.CouponRequestDto;
import shop.s5g.shop.dto.coupon.coupon.CouponResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.coupon.CouponBadRequestException;
import shop.s5g.shop.service.coupon.coupon.CouponService;
import shop.s5g.shop.service.coupon.coupon.RedisCouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/admin")
@ConditionalOnBean(RedisConfig.class)
public class CouponController {

    private final CouponService couponService;
    private final RedisCouponService redisCouponService;

    /**
     * 쿠폰 생성 API
     * @param couponRequestDto
     * @param bindingResult
     * @return ResponseEntity<MessageDto>
     */
    @PostMapping("/coupons")
    public ResponseEntity<MessageDto> createCoupon(
        @Valid @RequestBody CouponRequestDto couponRequestDto,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CouponBadRequestException("잘못된 데이터 요청입니다.");
        }

        redisCouponService.createCoupon(couponRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDto("쿠폰 생성 성공"));
    }

    /**
     * 발급 가능한 쿠폰 목록 가져오기
     * @param pageable
     * @return ResponseEntity<PageResponseDto<AvailableCouponResponseDto>>
     */
    @GetMapping("/coupons/available")
    public ResponseEntity<PageResponseDto<AvailableCouponResponseDto>> getAvailableCoupons(Pageable pageable) {

        Page<AvailableCouponResponseDto> coupons = couponService.getAvailableCoupons(pageable);

        return ResponseEntity.status(HttpStatus.OK)
            .body(PageResponseDto.of(coupons));
    }

    /**
     * 특정 쿠폰 조회 API
     * @param couponId
     * @return CouponResponseDto
     */
    @GetMapping("/coupons/{couponId}")
    public ResponseEntity<CouponResponseDto> findCoupon(@PathVariable("couponId") Long couponId) {
        CouponResponseDto couponResponseDto = couponService.getCoupon(couponId);

        return ResponseEntity.status(HttpStatus.OK).body(couponResponseDto);
    }

    /**
     * 쿠폰 삭제 API
     * @param couponId
     * @return MessageDto
     */
    @DeleteMapping("/coupons/{couponId}")
    public ResponseEntity<MessageDto> deleteCoupon(@PathVariable("couponId") Long couponId) {
        couponService.deleteCoupon(couponId);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageDto("쿠폰 삭제 성공"));
    }

    /**
     * 발급한 모든 쿠폰 조회 API
     * @param pageable
     * @return
     */
    @GetMapping("/coupons")
    public ResponseEntity<PageResponseDto<CouponResponseDto>> getAllCoupons(Pageable pageable) {

        Page<CouponResponseDto> couponList = couponService.getAllCouponList(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(PageResponseDto.of(couponList));
    }

}
