package shop.S5G.shop.repository.coupon.template.qdsl;

import shop.S5G.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.S5G.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.S5G.shop.entity.coupon.CouponPolicy;

public interface CouponTemplateQuerydslRepository {

    CouponTemplateResponseDto findCouponTemplateById(Long couponTemplateId);

    void updateCouponTemplate(Long couponTemplateId, CouponPolicy couponPolicy, CouponTemplateRequestDto couponTemplateRequestDto);

    boolean checkActiveCouponTemplate(Long couponTemplateId);

    void deleteCouponTemplate(Long couponTemplateId);
}
