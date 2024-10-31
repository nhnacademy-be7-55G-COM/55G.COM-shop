package shop.S5G.shop.dto.payments;

public record TossPaymentsConfirmRequestDto(
    String paymentKey,
    String orderId,
    long amount
) {

}
