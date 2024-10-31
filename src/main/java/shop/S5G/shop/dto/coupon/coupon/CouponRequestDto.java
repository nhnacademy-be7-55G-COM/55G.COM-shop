package shop.S5G.shop.dto.coupon.coupon;

import java.time.LocalDateTime;

public record CouponRequestDto(

    Long couponTemplateId,
    String couponCode,
    LocalDateTime createdAt
) {

}
