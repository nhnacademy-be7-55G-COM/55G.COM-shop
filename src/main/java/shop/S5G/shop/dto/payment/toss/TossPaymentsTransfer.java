package shop.S5G.shop.dto.payment.toss;

// 계좌이체
public record TossPaymentsTransfer(
    String bankCode,
    TossPaymentsSettlementStatus settlementStatus
) {

}
