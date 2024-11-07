package shop.S5G.shop.repository.order;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.order.DeliveryStatus;

public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatus, Long> {
    Optional<DeliveryStatus> findByName(String name);
}
