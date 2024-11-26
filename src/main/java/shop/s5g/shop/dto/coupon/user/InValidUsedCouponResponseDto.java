package shop.s5g.shop.dto.coupon.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InValidUsedCouponResponseDto(

    Long couponId,

    String couponCode,

    String couponName,

    LocalDateTime createdAt,

    LocalDateTime expiredAt,

    LocalDateTime usedAt,

    String couponDescription,

    Long condition,

    BigDecimal discountPrice,

    Long maxPrice

) {

}
