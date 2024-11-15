package shop.s5g.shop.dto.coupon.policy;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 쿠폰 정책 요청 Dto
 * @param discountPrice
 * @param condition
 * @param maxPrice
 * @param duration
 */
public record CouponPolicyRequestDto(

    @NotNull
    @Digits(integer = 6, fraction = 2)
    BigDecimal discountPrice,

    @NotNull
    @DecimalMin(value = "10000")
    @DecimalMax(value = "500000")
    Long condition,

    @DecimalMin(value = "1000")
    Long maxPrice,

    @NotNull
    @DecimalMin(value = "1")
    Integer duration
) {

}
