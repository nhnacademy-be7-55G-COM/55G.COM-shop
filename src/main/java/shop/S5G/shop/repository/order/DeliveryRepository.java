package shop.S5G.shop.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.order.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
}
