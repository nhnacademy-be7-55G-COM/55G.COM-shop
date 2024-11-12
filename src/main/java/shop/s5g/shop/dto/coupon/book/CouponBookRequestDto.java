package shop.s5g.shop.dto.coupon.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CouponBookRequestDto(
    @NotNull
    @Min(1)
    Long couponTemplateId,

    @NotNull
    @Min(1)
    Long bookId
) {

}
