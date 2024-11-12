package shop.s5g.shop.controller.coupon;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.coupon.coupon.CouponRequestDto;
import shop.s5g.shop.dto.coupon.coupon.CouponResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.coupon.CouponBadRequestException;
import shop.s5g.shop.service.coupon.coupon.CouponService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/admin")
public class CouponController {

    private final CouponService couponService;

    /**
     * 쿠폰 생성 API
     * @param couponCnt
     * @param couponRequestDto
     * @param bindingResult
     * @return ResponseEntity<MessageDto>
     */
    @PostMapping("/coupons")
    public ResponseEntity<MessageDto> createCoupon(
        @RequestParam(defaultValue = "1") Integer couponCnt,
        @Valid @RequestBody CouponRequestDto couponRequestDto,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CouponBadRequestException("잘못된 데이터 요청입니다.");
        }

        couponService.createCoupon(couponCnt, couponRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDto("쿠폰 생성 성공"));
    }

    /**
     * 쿠폰 업데이트 API
     * @param couponId
     * @param couponRequestDto
     * @param bindingResult
     * @return ResponseEntity<MessageDto>
     */
    @PatchMapping("/coupons/{couponId}")
    public ResponseEntity<MessageDto> updateCoupon(
        @PathVariable("couponId") Long couponId,
        @Valid @RequestBody CouponRequestDto couponRequestDto,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new CouponBadRequestException("잘못된 데이터 요청입니다.");
        }

        couponService.updateCoupon(couponId, couponRequestDto.expiredAt());

        return ResponseEntity.status(HttpStatus.OK).body(new MessageDto("쿠폰 업데이트 성공"));
    }

    /**
     * 쿠폰 목록 조회 API
     * @param pageable
     * @return List<CouponResponseDto>
     */
    @GetMapping("/coupons")
    public ResponseEntity<List<CouponResponseDto>> findCoupons(Pageable pageable) {
        List<CouponResponseDto> couponList = couponService.getCoupons(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(couponList);
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
}
