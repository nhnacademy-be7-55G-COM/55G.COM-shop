package shop.s5g.shop.repository.order;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.dto.delivery.DeliveryStatusView;
import shop.s5g.shop.entity.order.DeliveryStatus;

public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatus, Long> {
    Optional<DeliveryStatus> findByName(String name);
    List<DeliveryStatusView> findAllBy();
}
