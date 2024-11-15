package shop.s5g.shop.dto.payments.toss;

public record TossPaymentsRefundReceiveAccount(
    String bankCode,
    String accountNumber,
    String holderName
) {

}
