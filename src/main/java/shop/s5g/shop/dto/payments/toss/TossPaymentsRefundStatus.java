package shop.s5g.shop.dto.payments.toss;

// 환불 처리 상태.
public enum TossPaymentsRefundStatus {
    NONE, PENDING, FAILED, PARTIAL_FAILED, COMPLETED;
}
