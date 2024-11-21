package shop.s5g.shop.dto.coupon.template;

public record CouponTemplateUpdateRequestDto(
    Long couponTemplateId,
    String couponName,
    String couponDescription
) {

}
