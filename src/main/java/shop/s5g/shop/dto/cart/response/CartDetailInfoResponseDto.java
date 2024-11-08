package shop.s5g.shop.dto.cart.response;

import java.math.BigDecimal;

public record CartDetailInfoResponseDto(
    BigDecimal totalPrice,
    int deliveryFee,
    int freeShippingThreshold
) {

}
