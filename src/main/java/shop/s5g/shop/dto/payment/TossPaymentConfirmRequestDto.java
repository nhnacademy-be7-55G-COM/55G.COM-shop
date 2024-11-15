package shop.s5g.shop.dto.payment;

public record TossPaymentConfirmRequestDto(
    String paymentKey,
    String orderId,
    long amount
) {

}
