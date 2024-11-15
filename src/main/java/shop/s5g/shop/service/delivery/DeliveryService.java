package shop.s5g.shop.service.delivery;

import shop.s5g.shop.dto.delivery.DeliveryResponseDto;
import shop.s5g.shop.dto.delivery.DeliveryUpdateRequestDto;

public interface DeliveryService {

    DeliveryResponseDto getDelivery(long orderId);

    DeliveryResponseDto adminUpdateDelivery(DeliveryUpdateRequestDto updateRequest);

    DeliveryResponseDto userUpdateDelivery(DeliveryUpdateRequestDto updateRequest);
}
