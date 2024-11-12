package shop.s5g.shop.dto.coupon.category;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CouponCategoryRequestDto(
    @NotNull
    @Min(1)
    Long couponTemplateId,

    @NotNull
    @Min(1)
    Long categoryId
) {

}
