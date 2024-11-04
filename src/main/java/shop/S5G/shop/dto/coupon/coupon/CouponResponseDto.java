package shop.S5G.shop.dto.coupon.coupon;

import java.time.LocalDateTime;
import shop.S5G.shop.entity.coupon.CouponTemplate;

public record CouponResponseDto(

    CouponTemplate couponTemplate,

    String couponCode,

    LocalDateTime createdAt,

    LocalDateTime expiredAt,

    LocalDateTime usedAt

) {

}
