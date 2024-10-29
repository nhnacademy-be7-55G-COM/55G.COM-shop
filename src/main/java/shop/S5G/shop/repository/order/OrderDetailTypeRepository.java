package shop.S5G.shop.repository.order;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.order.OrderDetailType;

public interface OrderDetailTypeRepository extends JpaRepository<OrderDetailType, Long> {
    Optional<OrderDetailType> findByName(String name);
}
