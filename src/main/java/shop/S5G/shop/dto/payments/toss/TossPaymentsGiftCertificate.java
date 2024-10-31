package shop.S5G.shop.dto.payments.toss;

import jakarta.validation.constraints.Size;

public record TossPaymentsGiftCertificate(
    @Size(max = 8)
    String approveNo,
    TossPaymentsSettlementStatus settlementStatus
) {

}
