package shop.S5G.shop.dto.coupon.policy;

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
