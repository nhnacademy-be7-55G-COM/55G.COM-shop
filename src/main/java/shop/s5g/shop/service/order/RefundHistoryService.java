package shop.s5g.shop.service.order;

import shop.s5g.shop.dto.refund.RefundHistoryCreateRequestDto;
import shop.s5g.shop.dto.refund.RefundHistoryCreateResponseDto;
import shop.s5g.shop.dto.refund.RefundHistoryResponseDto;

public interface RefundHistoryService {

    RefundHistoryResponseDto getRefundHistory(long orderDetailId);

    RefundHistoryCreateResponseDto createNewRefund(long memberId, RefundHistoryCreateRequestDto createRequestDto);
}
