package shop.s5g.shop.dto.payments.toss;

public record TossPaymentsEasyPay(
    String provider,
    long amount,
    long discountAmount
) {

}
