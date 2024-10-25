package shop.S5G.shop.controller.coupon;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.dto.tag.MessageDto;
import shop.S5G.shop.dto.couponpolicy.CouponPolicyRequestDto;
import shop.S5G.shop.dto.couponpolicy.CouponPolicyResponseDto;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.service.coupon.CouponPolicyService;

/**
 * 쿠폰 정책 생성 컨트롤러
 * @author DooYoungHo
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/coupons")
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
    @PatchMapping("/policy/{couponPolicyId}")
    public ResponseEntity<MessageDto> updateCouponPolicy(
        CouponPolicyRequestDto couponPolicyRequestDto,
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
        CouponPolicyResponseDto couponPolicyResponseDto = couponPolicyService.findByCouponPolicyId(couponPolicyId);

        return ResponseEntity.status(HttpStatus.OK).body(couponPolicyResponseDto);
    }

    /**
     * 모든 쿠폰 정책 조회 - GET 요청
     * @return couponPolicyResponseDtoList
     */
    @GetMapping("/policy")
    public ResponseEntity<List<CouponPolicyResponseDto>> findAllCouponPolicy() {
        List<CouponPolicyResponseDto> couponPolicyResponseDtoList = couponPolicyService.findByAllCouponPolicies();

        return ResponseEntity.status(HttpStatus.OK).body(couponPolicyResponseDtoList);
    }
}
