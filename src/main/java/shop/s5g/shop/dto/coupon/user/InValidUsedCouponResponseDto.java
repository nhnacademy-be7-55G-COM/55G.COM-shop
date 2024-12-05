package shop.s5g.shop.dto.coupon.user;

import java.math.BigDecimal;

public record InValidUsedCouponResponseDto(

    Long couponId,

    String couponCode,

    String couponName,

    String couponDescription,

    Long condition,

    BigDecimal discountPrice,

    Long maxPrice

) {

}
