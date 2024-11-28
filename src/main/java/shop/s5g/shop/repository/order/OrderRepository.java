package shop.s5g.shop.repository.order;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.order.Order;
import shop.s5g.shop.repository.order.qdsl.OrderQuerydslRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderQuerydslRepository {
    Page<Order> findAllByCustomerCustomerId(long customerId, Pageable pageable);

    @EntityGraph(attributePaths = {"customer", "delivery"})
    Optional<Order> findOrderByUuid(String uuid);
}
