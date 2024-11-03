package shop.S5G.shop.dto.payment;

import shop.S5G.shop.dto.order.OrderCreateResponseDto;

public record TossPaymentConfirmRequestDto(
    String paymentKey,
    String orderId,
    long amount
) {

}
