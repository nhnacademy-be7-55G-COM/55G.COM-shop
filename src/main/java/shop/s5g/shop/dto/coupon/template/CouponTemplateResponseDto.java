package shop.s5g.shop.dto.coupon.template;

import shop.s5g.shop.entity.coupon.CouponPolicy;

/**
 * 쿠폰 템플릿 응답 Dto
 * @param couponPolicy
 * @param couponName
 * @param couponDescription
 */
public record CouponTemplateResponseDto(

    CouponPolicy couponPolicy,

    String couponName,

    String couponDescription

) {
}