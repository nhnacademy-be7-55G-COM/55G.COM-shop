package shop.s5g.shop.service.delivery.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.delivery.DeliveryResponseDto;
import shop.s5g.shop.dto.delivery.DeliveryUpdateRequestDto;
import shop.s5g.shop.entity.delivery.Delivery;
import shop.s5g.shop.entity.delivery.DeliveryStatus;
import shop.s5g.shop.entity.delivery.DeliveryStatus.Type;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.entity.order.OrderDetailType;
import shop.s5g.shop.exception.ResourceNotFoundException;
import shop.s5g.shop.repository.delivery.DeliveryRepository;
import shop.s5g.shop.repository.delivery.DeliveryStatusRepository;
import shop.s5g.shop.service.delivery.DeliveryService;

@Transactional
@RequiredArgsConstructor
@Service
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryStatusRepository deliveryStatusRepository;

    @Transactional(readOnly = true)
    @Override
    public DeliveryResponseDto getDelivery(long orderId) {
        return deliveryRepository.findByIdFetch(orderId).map(DeliveryResponseDto::of).orElseThrow(
            () -> new ResourceNotFoundException("orderId is not available")
        );
    }

    @Override
    public DeliveryResponseDto adminUpdateDelivery(DeliveryUpdateRequestDto updateRequest) {
        Delivery delivery = deliveryRepository.findByIdFetch(updateRequest.id()).orElseThrow(
            () -> new ResourceNotFoundException("deliveryId is not available")
        );

        if (!delivery.getStatus().getName().equals(updateRequest.status())) {
            DeliveryStatus newStatus = deliveryStatusRepository.findStatusByName(updateRequest.status());
            delivery.setStatus(newStatus);
        }

        delivery.setAddress(updateRequest.address());
        delivery.setShippingDate(updateRequest.shippingDate());
        delivery.setReceivedDate(updateRequest.receivedDate());
        delivery.setInvoiceNumber(updateRequest.invoiceNumber());

        return DeliveryResponseDto.of(delivery);
    }

    @Override
    public DeliveryResponseDto userUpdateDelivery(DeliveryUpdateRequestDto updateRequest) {
        Delivery delivery = deliveryRepository.findByIdFetch(updateRequest.id()).orElseThrow(
            () -> new ResourceNotFoundException("deliveryId is not available")
        );
        // 유저는 배송준비중인 상태에만 정보 수정 허용
        // TODO: 적절한 예외로 바꾸기
        if (updateRequest.status().equals("PREPARING"))
            throw new IllegalArgumentException();
        delivery.setAddress(updateRequest.address());
        delivery.setReceivedDate(updateRequest.receivedDate());

        return DeliveryResponseDto.of(delivery);
    }

    @Override
    public DeliveryStatus.Type deliveryCheckForCancel(long orderId) {
        Delivery delivery = deliveryRepository.findByIdFetchAllDetails(orderId).orElseThrow(
            () -> new ResourceNotFoundException("주문을 찾을 수 없습니다.")
        );
        boolean cancelFlag = true;

        List<OrderDetail> details = delivery.getOrder().getOrderDetails();
        for (OrderDetail detail: details) {
            // 만약 취소된게 아니라면,
            if (!detail.getOrderDetailType().getName().equals(OrderDetailType.Type.CANCEL.name())) {
                cancelFlag = false; // 배송취소는 아님.
                break;
            }
        }
        if (cancelFlag) {
            DeliveryStatus cancelStatus = deliveryStatusRepository.findStatusByName(Type.CANCELED);
            delivery.setStatus(cancelStatus);
        }
        return DeliveryStatus.Type.valueOf(delivery.getStatus().getName());
    }

}
