package shop.s5g.shop.service.delivery.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.delivery.DeliveryStatusView;
import shop.s5g.shop.repository.delivery.DeliveryStatusRepository;

@Service
@RequiredArgsConstructor
public class DeliveryStatusServiceImpl {
    private final DeliveryStatusRepository deliveryStatusRepository;

    public List<DeliveryStatusView> getAllStatus() {
        return deliveryStatusRepository.findAllBy();
    }
}
