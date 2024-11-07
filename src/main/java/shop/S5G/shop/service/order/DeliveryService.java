package shop.S5G.shop.service.order;

import shop.S5G.shop.dto.delivery.DeliveryCreateRequestDto;
import shop.S5G.shop.dto.delivery.DeliveryResponseDto;
import shop.S5G.shop.dto.delivery.DeliveryUpdateRequestDto;

public interface DeliveryService {

    DeliveryResponseDto getDelivery(long orderId);

    long saveAndGetId(DeliveryCreateRequestDto request);

    DeliveryResponseDto adminUpdateDelivery(DeliveryUpdateRequestDto updateRequest);

    DeliveryResponseDto userUpdateDelivery(DeliveryUpdateRequestDto updateRequest);
}
