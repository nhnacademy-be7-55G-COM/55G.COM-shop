package shop.s5g.shop.controller.coupon;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.coupon.CouponTemplateBadRequestException;
import shop.s5g.shop.service.coupon.template.CouponTemplateService;

/**
 * 쿠폰 템플릿 컨트롤러
 * @author DooYoungHo
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/admin/coupons")
public class CouponTemplateController {

    private final CouponTemplateService couponTemplateService;

    /**
     * 쿠폰 템플릿 생성 - POST 요청
     * @param couponTemplateRequestDto
     * @param bindingResult
     * @return messageDto
     */
    @PostMapping("/template")
    public ResponseEntity<MessageDto> createCouponTemplate(
        @Valid @RequestBody CouponTemplateRequestDto couponTemplateRequestDto,
        BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            throw new CouponTemplateBadRequestException("잘못된 요청입니다.");
        }

        couponTemplateService.createCouponTemplate(couponTemplateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new MessageDto("쿠폰 템플릿 생성 성공"));
    }

    /**
     * 쿠폰 템플릿 수정 - PATCH
     * @param couponTemplateId
     * @param couponTemplateRequestDto
     * @param bindingResult
     * @return messageDto
     */
    @PatchMapping("/template/{couponTemplateId}")
    public ResponseEntity<MessageDto> updateCouponTemplate(
        @PathVariable("couponTemplateId") Long couponTemplateId,
        @Valid @RequestBody CouponTemplateRequestDto couponTemplateRequestDto,
        BindingResult bindingResult
    ) {

        if (bindingResult.hasErrors()) {
            throw new CouponTemplateBadRequestException("잘못된 요청입니다.");
        }

        couponTemplateService.updateCouponTemplate(couponTemplateId, couponTemplateRequestDto);

        return ResponseEntity.status(HttpStatus.OK)
            .body(new MessageDto("쿠폰 템플릿 수정 성공"));
    }

    /**
     * 쿠폰 정책 조회 - GET
     * @param couponTemplateId
     * @return couponTemplateResponseDto
     */
    @GetMapping("/template/{couponTemplateId}")
    public ResponseEntity<CouponTemplateResponseDto> findCouponTemplate(
        @PathVariable("couponTemplateId") Long couponTemplateId
    ) {
        CouponTemplateResponseDto couponTemplateResponseDto = couponTemplateService.findCouponTemplate(couponTemplateId);

        return ResponseEntity.status(HttpStatus.OK)
            .body(couponTemplateResponseDto);
    }

    /**
     * 쿠폰 템플릿 삭제 - DELETE
     * @param couponTemplateId
     * @return messageDto
     */
    @DeleteMapping("/template/{couponTemplateId}")
    public ResponseEntity<MessageDto> deleteCouponTemplate(
        @PathVariable("couponTemplateId") Long couponTemplateId
    ) {
        couponTemplateService.deleteCouponTemplate(couponTemplateId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(new MessageDto("쿠폰 템플릿 삭제 성공"));
    }
}
