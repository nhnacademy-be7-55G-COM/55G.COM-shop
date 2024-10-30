package shop.S5G.shop.dto.payment.toss;

public record TossPaymentsEasyPay(
    String provider,
    long amount,
    long discountAmount
) {

}
