package shop.s5g.shop.dto.coupon.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ValidUserCouponResponseDto(

    Long couponId,

    Long couponTemplateId,

    String couponCode,

    String couponName,

    String couponDescription,

    Long condition,

    BigDecimal discountPrice,

    Long maxPrice
) {

}
