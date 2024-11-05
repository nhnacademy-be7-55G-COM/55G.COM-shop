package shop.S5G.shop.service.order;

import shop.S5G.shop.dto.refund.RefundHistoryCreateRequestDto;
import shop.S5G.shop.dto.refund.RefundHistoryResponseDto;

public interface RefundHistoryService {

    RefundHistoryResponseDto getRefundHistory(long orderDetailId);

    void save(RefundHistoryCreateRequestDto createRequestDto);
}
