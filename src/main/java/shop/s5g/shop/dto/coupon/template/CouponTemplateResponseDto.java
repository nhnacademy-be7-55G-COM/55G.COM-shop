package shop.s5g.shop.dto.coupon.template;

import java.math.BigDecimal;


public record CouponTemplateResponseDto(

    BigDecimal discountPrice,

    Long condition,

    Long maxPrice,

    Integer duration,

    String couponName,

    String couponDescription

) {
}
