package shop.S5G.shop.repository.order.qdsl;

import shop.S5G.shop.dto.refund.RefundHistoryResponseDto;

public interface RefundHistoryQuerydslRepository {

    RefundHistoryResponseDto fetchRefundHistoryByOrderDetailId(long orderDetailId);
}
