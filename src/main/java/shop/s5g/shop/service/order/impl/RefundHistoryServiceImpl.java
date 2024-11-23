package shop.s5g.shop.service.order.impl;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.point.PointHistoryCreateRequestDto;
import shop.s5g.shop.dto.refund.OrderDetailDeliveryPairContainer;
import shop.s5g.shop.dto.refund.RefundHistoryCreateRequestDto;
import shop.s5g.shop.dto.refund.RefundHistoryCreateResponseDto;
import shop.s5g.shop.dto.refund.RefundHistoryResponseDto;
import shop.s5g.shop.entity.delivery.Delivery;
import shop.s5g.shop.entity.delivery.DeliveryFee;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.entity.order.OrderDetailType;
import shop.s5g.shop.entity.order.OrderDetailType.Type;
import shop.s5g.shop.entity.refund.RefundHistory;
import shop.s5g.shop.entity.refund.RefundImage;
import shop.s5g.shop.entity.refund.RefundType;
import shop.s5g.shop.exception.EssentialDataNotFoundException;
import shop.s5g.shop.exception.refund.RefundAlreadyProceedException;
import shop.s5g.shop.exception.refund.RefundConditionNotFulfilledException;
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

    public static final long BASIC_REFUND_DAY_LIMITATION = 10L;
    public static final long DAMAGED_REFUND_DAY_LIMITATION = 30L;

    @Transactional(readOnly = true)
    @Override
    public RefundHistoryResponseDto getRefundHistory(long orderDetailId) {
        return refundHistoryRepository.fetchRefundHistoryByOrderDetailId(orderDetailId);
    }

    @Override
    public RefundHistoryCreateResponseDto createNewRefund(long memberId, RefundHistoryCreateRequestDto createRequestDto) {
        OrderDetailDeliveryPairContainer pair = orderDetailRepository.fetchOrderDetailAndDelivery(createRequestDto.orderDetailId());

        RefundType refundType = refundTypeRepository.findTypeById(createRequestDto.typeId());
        OrderDetail detail = pair.orderDetail();
        Delivery delivery = pair.delivery();

        // 반품 조건 체크 겸 포인트 적립.
        if (refundType.getId() == RefundType.Type.DAMAGED.getTypeId()) {
            damagedRefundPoint(memberId, detail, delivery);
        } else {
            basicRefundPoint(memberId, detail, delivery);
        }

        // EssentialDataNotFoundException
        OrderDetailType detailType = orderDetailTypeRepository.findStatusByName(Type.RETURN);

        if (detail.getOrderDetailType() == detailType) {
            throw RefundAlreadyProceedException.INSTANCE;
        }

        detail.setOrderDetailType(detailType);  // REFUND 타입으로 바꿈.

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

        return new RefundHistoryCreateResponseDto(savedHistory.getId());
    }

    private void basicRefundPoint(long memberId, OrderDetail detail, Delivery delivery) {
        try {
            LocalDate now = LocalDate.now();
            LocalDate limit = delivery.getShippingDate().plusDays(BASIC_REFUND_DAY_LIMITATION);

            if (now.isAfter(limit)) {
                throw new RefundConditionNotFulfilledException("일반 반품은 10일 이내 미사용 시에만 반품 가능합니다.");
            }
            DeliveryFee fee = delivery.getDeliveryFee();

            // 결제한 금액에서 적립된 금액과 환불배송비의 차이만큼 적립.
            PointHistoryCreateRequestDto point = new PointHistoryCreateRequestDto(
                "환불",
                detail.getTotalPrice() - detail.getAccumulationPrice() - fee.getRefundFee()
            );

            pointHistoryService.createPointHistory(memberId, point);
        }catch (NullPointerException e) {
            throw new EssentialDataNotFoundException("환불 배송정보가 존재하지 않음.");
        }
    }

    private void damagedRefundPoint(long memberId, OrderDetail detail, Delivery delivery) {
        try {
            LocalDate now = LocalDate.now();
            LocalDate limit = delivery.getShippingDate().plusDays(DAMAGED_REFUND_DAY_LIMITATION);

            if (now.isAfter(limit)) {
                throw new RefundConditionNotFulfilledException("파손/파본에 의한 반품은 출고일 기준 30일 까지 가능합니다.");
            }

            // 환불 배송비 무료
            PointHistoryCreateRequestDto point = new PointHistoryCreateRequestDto(
                "환불",
                detail.getTotalPrice() - detail.getAccumulationPrice()
            );

            pointHistoryService.createPointHistory(memberId, point);
        }catch (NullPointerException e) {
            throw new EssentialDataNotFoundException("환불 배송정보가 존재하지 않음.");
        }
    }
}
