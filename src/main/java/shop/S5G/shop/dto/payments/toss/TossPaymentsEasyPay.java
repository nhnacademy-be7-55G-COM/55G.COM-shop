package shop.S5G.shop.dto.payments.toss;

public record TossPaymentsEasyPay(
    String provider,
    long amount,
    long discountAmount
) {

}
