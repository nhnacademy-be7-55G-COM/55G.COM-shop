package shop.s5g.shop.dto.coupon.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InValidUsedCouponResponseDto(
    @NotNull
    Long couponId,

    @NotBlank
    String couponCode,

    @NotNull
    String couponName,

    @NotNull
    LocalDateTime createdAt,

    @NotNull
    LocalDateTime expiredAt,

    @NotNull
    LocalDateTime usedAt,

    @NotBlank
    String couponDescription,

    @NotNull
    Long condition,

    @NotNull
    BigDecimal discountPrice,

    Long maxPrice

) {

}
