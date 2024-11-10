package shop.s5g.shop.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.order.Delivery;
import shop.s5g.shop.repository.order.qdsl.DeliveryQuerydslRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long>,
    DeliveryQuerydslRepository {

}
