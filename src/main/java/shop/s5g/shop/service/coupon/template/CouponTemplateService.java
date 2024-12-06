package shop.s5g.shop.service.coupon.template;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateUpdateRequestDto;

public interface CouponTemplateService {

    // Create
    void createCouponTemplate(CouponTemplateRequestDto couponTemplateRequestDto);

    // Read
    CouponTemplateResponseDto getCouponTemplate(Long couponTemplateId);

    // Update
    void updateCouponTemplate(Long couponTemplateId,
        CouponTemplateUpdateRequestDto couponTemplateUpdateRequestDto);

    // Delete
    void deleteCouponTemplate(Long couponTemplateId);

    // Read - Pageable
    Page<CouponTemplateResponseDto> getCouponTemplates(Pageable pageable);

    Page<CouponTemplateResponseDto> getCouponTemplatesUnused(Pageable pageable);

    Page<CouponTemplateResponseDto> getCouponTemplateExcludingWelcomeAndBirth(Pageable pageable);
}
