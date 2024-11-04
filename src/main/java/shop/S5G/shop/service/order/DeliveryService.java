package shop.S5G.shop.service.order;

import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.delivery.DeliveryCreateRequestDto;
import shop.S5G.shop.dto.delivery.DeliveryResponseDto;

public interface DeliveryService {

    @Transactional(readOnly = true)
    DeliveryResponseDto findById(long orderId);

    long saveAndGetId(DeliveryCreateRequestDto request);
}
