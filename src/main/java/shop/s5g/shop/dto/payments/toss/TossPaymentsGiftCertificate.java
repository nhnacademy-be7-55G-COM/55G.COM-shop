package shop.s5g.shop.dto.payments.toss;

import jakarta.validation.constraints.Size;

public record TossPaymentsGiftCertificate(
    @Size(max = 8)
    String approveNo,
    TossPaymentsSettlementStatus settlementStatus
) {

}
