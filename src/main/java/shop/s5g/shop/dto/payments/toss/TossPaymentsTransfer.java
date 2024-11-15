package shop.s5g.shop.dto.payments.toss;

// 계좌이체
public record TossPaymentsTransfer(
    String bankCode,
    TossPaymentsSettlementStatus settlementStatus
) {

}
