package shop.s5g.shop.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.order.DeliveryFee;

public interface DeliveryFeeRepository extends JpaRepository<DeliveryFee, Long> {

}
