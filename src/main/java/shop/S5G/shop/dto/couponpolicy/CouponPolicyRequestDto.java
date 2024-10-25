package shop.S5G.shop.dto.couponpolicy;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CouponPolicyRequestDto(

    @Min(0)
    @NotNull
    BigDecimal discountPrice,

    @NotNull
    Long condition,

    Long maxPrice,

    @NotNull
    Integer duration
) {

}