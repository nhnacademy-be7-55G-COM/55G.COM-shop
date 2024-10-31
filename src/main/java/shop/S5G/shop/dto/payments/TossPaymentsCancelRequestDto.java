package shop.S5G.shop.dto.payments;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import shop.S5G.shop.dto.payments.toss.TossPaymentsRefundReceiveAccount;

@JsonInclude(Include.NON_NULL)
public record TossPaymentsCancelRequestDto(
    String cancelReason,    // 필수
    Long cancelAmount,  // 0이면 전액
    // 가상계좌결제에서 필수
    TossPaymentsRefundReceiveAccount refundReceiveAccount,
    Long taxFreeAmount, // 기본값 0
    String currency
) {

}
