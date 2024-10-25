package shop.S5G.shop.dto.couponpolicy;

import java.math.BigDecimal;

public record CouponPolicyResponseDto (
    BigDecimal discountPrice,
    Long condition,
    Long maxPrice,
    Integer duration
) {

}
