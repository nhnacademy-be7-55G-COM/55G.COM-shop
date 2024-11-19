package shop.s5g.shop.dto.payments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import shop.s5g.shop.dto.payments.toss.TossPaymentsRefundReceiveAccount;

@JsonInclude(Include.NON_NULL)
public record TossPaymentsCancelRequestDto(
    @NotNull
    String cancelReason,    // 필수
    @Min(0)
    Long cancelAmount,  // 0이면 전액
    // 가상계좌결제에서 필수
    @Nullable
    TossPaymentsRefundReceiveAccount refundReceiveAccount,
    @Min(0)
    Long taxFreeAmount, // 기본값 0
    @NotNull
    String currency
) {

}
