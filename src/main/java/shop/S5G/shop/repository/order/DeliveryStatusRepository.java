package shop.S5G.shop.repository.order;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.dto.delivery.DeliveryStatusView;
import shop.S5G.shop.entity.order.DeliveryStatus;

public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatus, Long> {
    Optional<DeliveryStatus> findByName(String name);
    List<DeliveryStatusView> findAllBy();
}
