package shop.S5G.shop.dto.payment;

public record TossPaymentConfirmRequestDto(
    String paymentKey,
    String orderId,
    long amount
) {

}
