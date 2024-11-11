package shop.s5g.shop.repository.delivery;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.delivery.DeliveryFee;
import shop.s5g.shop.repository.delivery.qdsl.DeliveryFeeQuerydslRepository;

public interface DeliveryFeeRepository extends JpaRepository<DeliveryFee, Long>,
    DeliveryFeeQuerydslRepository {
}
