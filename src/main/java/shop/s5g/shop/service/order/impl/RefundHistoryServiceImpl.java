package shop.s5g.shop.service.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.point.PointHistoryCreateRequestDto;
import shop.s5g.shop.dto.refund.RefundHistoryCreateRequestDto;
import shop.s5g.shop.dto.refund.RefundHistoryCreateResponseDto;
import shop.s5g.shop.dto.refund.RefundHistoryResponseDto;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.entity.order.OrderDetailType;
import shop.s5g.shop.entity.order.OrderDetailType.Type;
import shop.s5g.shop.entity.refund.RefundHistory;
import shop.s5g.shop.entity.refund.RefundImage;
import shop.s5g.shop.entity.refund.RefundType;
import shop.s5g.shop.exception.order.OrderDetailsNotExistException;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.repository.order.OrderDetailTypeRepository;
import shop.s5g.shop.repository.order.RefundHistoryRepository;
import shop.s5g.shop.repository.order.RefundImageRepository;
import shop.s5g.shop.repository.order.RefundTypeRepository;
import shop.s5g.shop.service.order.RefundHistoryService;
import shop.s5g.shop.service.point.PointHistoryService;

@Transactional
@RequiredArgsConstructor
@Service
public class RefundHistoryServiceImpl implements RefundHistoryService {
    private final RefundHistoryRepository refundHistoryRepository;
    private final RefundImageRepository refundImageRepository;
    private final RefundTypeRepository refundTypeRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PointHistoryService pointHistoryService;
    private final OrderDetailTypeRepository orderDetailTypeRepository;

    @Transactional(readOnly = true)
    @Override
    public RefundHistoryResponseDto getRefundHistory(long orderDetailId) {
        return refundHistoryRepository.fetchRefundHistoryByOrderDetailId(orderDetailId);
    }

    @Override
    public RefundHistoryCreateResponseDto createNewRefund(long memberId, RefundHistoryCreateRequestDto createRequestDto) {
        OrderDetail detail = orderDetailRepository.findById(createRequestDto.orderDetailId()).orElseThrow(
            () -> new OrderDetailsNotExistException("환불 대상이 존재하지 않음")
        );

        // EssentialDataNotFoundException
        OrderDetailType detailType = orderDetailTypeRepository.findStatusByName(Type.RETURN);
        detail.setOrderDetailType(detailType);
        RefundType refundType = refundTypeRepository.findTypeById(createRequestDto.typeId());

        RefundHistory history = new RefundHistory(detail, refundType, createRequestDto.reason());
        RefundHistory savedHistory = refundHistoryRepository.save(history);

        // 이미지 저장
        if (createRequestDto.images() != null) {
            refundImageRepository.saveAll(
                createRequestDto.images().stream()
                    .map(image -> new RefundImage(savedHistory, image))
                    .toList()
            );
        }
        // 결제한 금액에서 적립된 금액의 차이만큼 적립.
        PointHistoryCreateRequestDto point = new PointHistoryCreateRequestDto("환불", detail.getTotalPrice() - detail.getAccumulationPrice());

        pointHistoryService.createPointHistory(memberId, point);
        return new RefundHistoryCreateResponseDto(savedHistory.getId());
    }

}
