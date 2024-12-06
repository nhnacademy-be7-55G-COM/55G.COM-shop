package shop.s5g.shop.dto.coupon.coupon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CouponRequestDto(
    @Min(1)
    Integer quantity,

    @NotNull
    @Min(1)
    Long couponTemplateId
) {

}
