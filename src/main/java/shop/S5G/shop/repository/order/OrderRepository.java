package shop.S5G.shop.repository.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.order.Order;
import shop.S5G.shop.repository.order.qdsl.OrderQuerydslRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderQuerydslRepository {
    Page<Order> findAllByCustomerCustomerId(long customerId, Pageable pageable);
}
