package shop.S5G.shop.dto.coupon.template;

import shop.S5G.shop.entity.coupon.CouponPolicy;

public record CouponTemplateRequestDto(

    CouponPolicy couponPolicy,

    String couponName,

    String couponDescription

) {

}
