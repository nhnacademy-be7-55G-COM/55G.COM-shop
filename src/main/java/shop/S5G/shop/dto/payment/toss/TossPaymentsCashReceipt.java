package shop.S5G.shop.dto.payment.toss;

import jakarta.validation.constraints.Size;

// 현금영수증
public record TossPaymentsCashReceipt(
    String type,    // TODO: 소득공제, 지출증빙
    String receiptKey,
    @Size(max = 9)
    String issueNumber,
    String receiptUrl,
    long amount,
    long taxFreeAmount
) {

}
