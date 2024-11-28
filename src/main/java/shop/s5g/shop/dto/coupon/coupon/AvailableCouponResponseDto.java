package shop.s5g.shop.dto.coupon.coupon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AvailableCouponResponseDto(

    Long couponId,

    Long couponTemplateId,

    String couponName,

    Long condition,

    Long maxPrice,

    BigDecimal discountPrice,

    Long count
) {

}
