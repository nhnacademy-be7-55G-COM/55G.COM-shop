package shop.S5G.shop.dto.payments.toss;

public record TossPaymentsRefundReceiveAccount(
    String bankCode,
    String accountNumber,
    String holderName
) {

}
