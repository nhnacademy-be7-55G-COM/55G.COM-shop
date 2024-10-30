package shop.S5G.shop.repository.coupon.template.qdsl;

import shop.S5G.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.S5G.shop.dto.coupon.template.CouponTemplateResponseDto;

public interface CouponTemplateQuerydslRepository {

    CouponTemplateResponseDto findCouponTemplateById(Long couponTemplateId);

    void updateCouponTemplate(Long couponTemplateId, CouponTemplateRequestDto couponTemplateRequestDto);

    boolean checkActiveCouponTemplate(Long couponTemplateId);

    void deleteCouponTemplate(Long couponTemplateId);
}
