package shop.S5G.shop.dto.couponpolicy;

import java.math.BigDecimal;

/**
 * 쿠폰 정책 응답 Dto
 * @param discountPrice
 * @param condition
 * @param maxPrice
 * @param duration
 */
public record CouponPolicyResponseDto (
    BigDecimal discountPrice,
    Long condition,
    Long maxPrice,
    Integer duration
) {

}
