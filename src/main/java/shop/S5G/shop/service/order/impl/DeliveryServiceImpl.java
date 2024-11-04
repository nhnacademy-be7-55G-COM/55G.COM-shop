package shop.S5G.shop.service.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.delivery.DeliveryCreateRequestDto;
import shop.S5G.shop.dto.delivery.DeliveryResponseDto;
import shop.S5G.shop.entity.order.Delivery;
import shop.S5G.shop.entity.order.DeliveryFee;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.exception.ResourceNotFoundException;
import shop.S5G.shop.repository.order.DeliveryFeeRepository;
import shop.S5G.shop.repository.order.DeliveryRepository;

@Transactional
@RequiredArgsConstructor
@Service
public class DeliveryServiceImpl implements shop.S5G.shop.service.order.DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryFeeRepository deliveryFeeRepository;

    @Transactional(readOnly = true)
    @Override
    public DeliveryResponseDto findById(long orderId) {
        return deliveryRepository.findById(orderId).map(DeliveryResponseDto::of).orElseThrow(
            () -> new ResourceNotFoundException("orderId is not available")
        );
    }

    @Override
    public long saveAndGetId(DeliveryCreateRequestDto request) {
        DeliveryFee fee = deliveryFeeRepository.findById(request.deliveryFeeId()).orElseThrow(
            () -> new BadRequestException("Can't find delivery fee")
        );
        // TODO: 타입 문제
        return deliveryRepository.save(new Delivery(request.address(), request.receivedDate(), (int) fee.getFee())).getId();
    }
    // TODO: update 메소드
//    public long updateDelivery(long id) {
//
//    }


}
