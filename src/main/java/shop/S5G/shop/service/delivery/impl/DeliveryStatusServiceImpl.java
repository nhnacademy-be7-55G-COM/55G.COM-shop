package shop.S5G.shop.service.delivery.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.S5G.shop.dto.delivery.DeliveryStatusView;
import shop.S5G.shop.repository.order.DeliveryStatusRepository;

@Service
@RequiredArgsConstructor
public class DeliveryStatusServiceImpl {
    private final DeliveryStatusRepository deliveryStatusRepository;

    public List<DeliveryStatusView> getAllStatus() {
        return deliveryStatusRepository.findAllBy();
    }
}
