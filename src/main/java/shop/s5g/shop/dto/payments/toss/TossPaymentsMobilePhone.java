package shop.s5g.shop.dto.payments.toss;

import jakarta.validation.constraints.Size;

public record TossPaymentsMobilePhone(
    @Size(min=8, max=15)
    String customerMobilePhone,
    TossPaymentsSettlementStatus settlementStatus,
    String receiptUrl
) {

}
