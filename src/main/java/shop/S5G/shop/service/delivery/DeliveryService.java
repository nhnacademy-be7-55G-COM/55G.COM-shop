package shop.S5G.shop.service.delivery;

import shop.S5G.shop.dto.delivery.DeliveryResponseDto;
import shop.S5G.shop.dto.delivery.DeliveryUpdateRequestDto;

public interface DeliveryService {

    DeliveryResponseDto getDelivery(long orderId);

    DeliveryResponseDto adminUpdateDelivery(DeliveryUpdateRequestDto updateRequest);

    DeliveryResponseDto userUpdateDelivery(DeliveryUpdateRequestDto updateRequest);
}
