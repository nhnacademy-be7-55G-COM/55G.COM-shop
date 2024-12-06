package shop.s5g.shop.dto.coupon.user;

import java.time.LocalDateTime;

public record UserCouponResponseDto(

    Long couponId,

    String couponCode,

    LocalDateTime createdAt,

    LocalDateTime expiredAt,

    String couponName,

    String couponDescription

) {

}
