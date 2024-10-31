package shop.S5G.shop.dto.payments.toss;

import org.springframework.lang.Nullable;

// https://docs.tosspayments.com/reference
public record TossPaymentsCard(
    long amount,
    String issuerCode,
    @Nullable
    String acquirerCode,
    String number,
    int installmentPlanMonths,  // 할부 개월 수
    String approveNo,
    boolean useCardPoint,
    TossPaymentsCardType cardType,
    TossPaymentsOwnerType ownerType,
    TossPaymentsAcquireStatus acquireStatus,
    boolean isInterestFee,
    @Nullable
    TossPaymentsInterestPayer interestPayer
) {

}
