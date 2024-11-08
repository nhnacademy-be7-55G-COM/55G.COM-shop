package shop.s5g.shop.repository.order.qdsl;

import shop.s5g.shop.dto.refund.RefundHistoryResponseDto;

public interface RefundHistoryQuerydslRepository {

    RefundHistoryResponseDto fetchRefundHistoryByOrderDetailId(long orderDetailId);
}
