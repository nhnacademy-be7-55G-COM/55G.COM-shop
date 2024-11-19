package shop.s5g.shop.dto.payments;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record TossPaymentsCancelSimpleRequestDto(
    @NotNull
    String cancelReason,
    @Min(0)
    long cancelAmount
) {

}
