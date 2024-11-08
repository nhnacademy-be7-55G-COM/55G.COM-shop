package shop.s5g.shop.repository.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.order.Order;
import shop.s5g.shop.repository.order.qdsl.OrderQuerydslRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderQuerydslRepository {
    Page<Order> findAllByCustomerCustomerId(long customerId, Pageable pageable);
}
