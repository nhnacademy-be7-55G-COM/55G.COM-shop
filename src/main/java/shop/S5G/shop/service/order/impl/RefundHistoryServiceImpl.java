package shop.S5G.shop.service.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.refund.RefundHistoryCreateRequestDto;
import shop.S5G.shop.dto.refund.RefundHistoryResponseDto;
import shop.S5G.shop.repository.order.RefundHistoryRepository;
import shop.S5G.shop.service.order.RefundHistoryService;

@Transactional
@RequiredArgsConstructor
@Service
public class RefundHistoryServiceImpl implements RefundHistoryService {
    private final RefundHistoryRepository refundHistoryRepository;

    @Transactional(readOnly = true)
    @Override
    public RefundHistoryResponseDto getHistoryByOrderDetailId(long orderDetailId) {
        return refundHistoryRepository.fetchRefundHistoryByOrderDetailId(orderDetailId);
    }

    // TODO: 나머지 채우기.
    @Override
    public void save(RefundHistoryCreateRequestDto createRequestDto) {

    }
}
