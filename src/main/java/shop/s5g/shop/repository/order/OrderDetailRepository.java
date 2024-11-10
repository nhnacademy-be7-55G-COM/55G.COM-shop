package shop.s5g.shop.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.repository.order.qdsl.OrderDetailQuerydslRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>,
    OrderDetailQuerydslRepository {

    long countOrderDetailsByOrderId(long orderId);
}
