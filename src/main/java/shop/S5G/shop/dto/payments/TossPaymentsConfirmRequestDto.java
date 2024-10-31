package shop.S5G.shop.dto.payments;

import java.util.Map;

public record TossPaymentsConfirmRequestDto(
    String paymentKey,
    String orderId,
    Long amount
) {
    public static TossPaymentsConfirmRequestDto of(Map<String, String> body) {
        return new TossPaymentsConfirmRequestDto(
            body.get("paymentKey"),
            body.get("orderId"),
            Long.valueOf(body.get("amount"))
        );
    }
}
