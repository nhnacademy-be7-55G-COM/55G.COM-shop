package shop.S5G.shop.dto.payment.toss;

import jakarta.validation.constraints.Size;

public record TossPaymentsMobilePhone(
    @Size(min=8, max=15)
    String customerMobilePhone,
    TossPaymentsSettlementStatus settlementStatus,
    String receiptUrl
) {

}
