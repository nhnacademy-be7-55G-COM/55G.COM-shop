package shop.S5G.shop.service.order;

import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.refund.RefundHistoryCreateRequestDto;
import shop.S5G.shop.dto.refund.RefundHistoryResponseDto;

public interface RefundHistoryService {

    @Transactional(readOnly = true)
    RefundHistoryResponseDto getHistoryByOrderDetailId(long orderDetailId);

    // TODO: 나머지 채우기.
    void save(RefundHistoryCreateRequestDto createRequestDto);
}
