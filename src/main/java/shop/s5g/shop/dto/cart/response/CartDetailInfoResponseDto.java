package shop.s5g.shop.dto.cart.response;

import java.math.BigDecimal;

public record CartDetailInfoResponseDto(
    BigDecimal totalPrice,
    long deliveryFee,
    long freeShippingThreshold
) {

}
