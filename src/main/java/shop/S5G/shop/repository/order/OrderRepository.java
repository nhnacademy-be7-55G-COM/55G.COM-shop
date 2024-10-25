package shop.S5G.shop.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.order.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
