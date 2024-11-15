package shop.s5g.shop.service.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.refund.RefundHistoryCreateRequestDto;
import shop.s5g.shop.dto.refund.RefundHistoryResponseDto;
import shop.s5g.shop.repository.order.RefundHistoryRepository;
import shop.s5g.shop.service.order.RefundHistoryService;

@Transactional
@RequiredArgsConstructor
@Service
public class RefundHistoryServiceImpl implements RefundHistoryService {
    private final RefundHistoryRepository refundHistoryRepository;

    @Transactional(readOnly = true)
    @Override
    public RefundHistoryResponseDto getRefundHistory(long orderDetailId) {
        return refundHistoryRepository.fetchRefundHistoryByOrderDetailId(orderDetailId);
    }

    // TODO: 나머지 채우기.
    @Override
    public void save(RefundHistoryCreateRequestDto createRequestDto) {

    }
}
