package shop.s5g.shop.repository.order;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.order.OrderDetailType;

public interface OrderDetailTypeRepository extends JpaRepository<OrderDetailType, Long> {
    Optional<OrderDetailType> findByName(String name);
}
