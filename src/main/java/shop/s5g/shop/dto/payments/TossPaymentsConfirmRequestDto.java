package shop.s5g.shop.dto.payments;

import java.util.Map;

public record TossPaymentsConfirmRequestDto(
    String paymentKey,
    String orderId,
    Long amount
) {
    public static TossPaymentsConfirmRequestDto of(Map<String, Object> body) {
        return new TossPaymentsConfirmRequestDto(
            (String) body.get("paymentKey"),
            (String) body.get("orderId"),
            ((Number) body.get("amount")).longValue()
        );
    }
}
