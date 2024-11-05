package shop.S5G.shop.service.order;

import shop.S5G.shop.dto.delivery.DeliveryCreateRequestDto;
import shop.S5G.shop.dto.delivery.DeliveryResponseDto;

public interface DeliveryService {

    DeliveryResponseDto getDelivery(long orderId);

    long saveAndGetId(DeliveryCreateRequestDto request);
}
