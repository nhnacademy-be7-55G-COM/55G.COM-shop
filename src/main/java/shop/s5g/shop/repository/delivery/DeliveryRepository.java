package shop.s5g.shop.repository.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.delivery.Delivery;
import shop.s5g.shop.repository.delivery.qdsl.DeliveryQuerydslRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long>,
    DeliveryQuerydslRepository {

}
