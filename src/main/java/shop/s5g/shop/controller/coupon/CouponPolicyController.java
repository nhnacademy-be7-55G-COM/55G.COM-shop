package shop.s5g.shop.controller.coupon;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.dto.coupon.policy.CouponPolicyRequestDto;
import shop.s5g.shop.dto.coupon.policy.CouponPolicyResponseDto;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.service.coupon.policy.CouponPolicyService;

/**
 * 쿠폰 정책 생성 컨트롤러
 * @author DooYoungHo
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/admin/coupons")
public class CouponPolicyController {

    private final CouponPolicyService couponPolicyService;

    /**
     * 쿠폰 정책 생성 - POST 요청
     * @param couponPolicyRequestDto
     * @param bindingResult
     * @return MessageDto
     */
    @PostMapping("/policy")
    public ResponseEntity<MessageDto> createCouponPolicy(
        @Valid @RequestBody CouponPolicyRequestDto couponPolicyRequestDto,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("잘못된 요청입니다");
        }

        couponPolicyService.saveCouponPolicy(couponPolicyRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageDto("쿠폰 정책 생성 성공"));
    }

    /**
     * 쿠폰 정책 업데이트 - PATCH 요청
     * @param couponPolicyRequestDto
     * @param bindingResult
     * @param couponPolicyId
     * @return MessageDto
     */
    @PostMapping("/policy/{couponPolicyId}")
    public ResponseEntity<MessageDto> updateCouponPolicy(
        @Valid @RequestBody CouponPolicyRequestDto couponPolicyRequestDto,
        BindingResult bindingResult,
        @PathVariable("couponPolicyId") Long couponPolicyId) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException("잘못된 요청입니다.");
        }

        couponPolicyService.updateCouponPolicy(couponPolicyId, couponPolicyRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(new MessageDto("쿠폰 정책 수정 성공"));
    }

    /**
     * 특정 쿠폰 정책 조회 - GET 요청
     * @param couponPolicyId
     * @return couponPolicyResponseDto
     */
    @GetMapping("/policy/{couponPolicyId}")
    public ResponseEntity<CouponPolicyResponseDto> findCouponPolicyById(@PathVariable("couponPolicyId") Long couponPolicyId) {
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.getByCouponPolicyId(couponPolicyId);

        return ResponseEntity.status(HttpStatus.OK).body(couponPolicyResponseDto);
    }

    /**
     * 모든 쿠폰 정책 조회 - GET 요청
     * @return couponPolicyResponseDtoList
     */
    @GetMapping("/policy")
    public ResponseEntity<PageResponseDto<CouponPolicyResponseDto>> findAllCouponPolicy(Pageable pageable) {
        Page<CouponPolicyResponseDto> couponPolicyResponseDtoList = couponPolicyService.getAllCouponPolices(pageable);

        return ResponseEntity.status(HttpStatus.OK).body(PageResponseDto.of(couponPolicyResponseDtoList));
    }

}