package shop.s5g.shop.dto.coupon.coupon;

import java.time.LocalDateTime;
import shop.s5g.shop.entity.coupon.CouponTemplate;

public record CouponResponseDto(

    CouponTemplate couponTemplate,

    String couponCode,

    LocalDateTime createdAt,

    LocalDateTime expiredAt,

    LocalDateTime usedAt

) {

}
