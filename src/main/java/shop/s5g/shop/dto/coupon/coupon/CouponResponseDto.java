package shop.s5g.shop.dto.coupon.coupon;

import java.time.LocalDateTime;

public record CouponResponseDto(

    Long couponId,

    Long couponTemplateId,

    String couponCode,

    LocalDateTime createdAt,

    LocalDateTime expiredAt,

    LocalDateTime usedAt

) {

}
