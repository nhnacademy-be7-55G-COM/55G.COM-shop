package shop.s5g.shop.service.coupon.template;

import shop.s5g.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;

public interface CouponTemplateService {

    // Create
    void createCouponTemplate(CouponTemplateRequestDto couponTemplateRequestDto);

    // Read
    CouponTemplateResponseDto findCouponTemplate(Long couponTemplateId);

    // Update
    void updateCouponTemplate(Long couponTemplateId, CouponTemplateRequestDto couponTemplateRequestDto);

    // Delete
    void deleteCouponTemplate(Long couponTemplateId);
}
