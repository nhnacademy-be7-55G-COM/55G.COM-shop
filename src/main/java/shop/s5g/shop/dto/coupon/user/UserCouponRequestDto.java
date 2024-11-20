package shop.s5g.shop.dto.coupon.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserCouponRequestDto(

    @NotNull
    @Min(1)
    Long customerId
) {

}
