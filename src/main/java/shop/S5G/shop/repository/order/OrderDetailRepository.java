package shop.S5G.shop.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.order.OrderDetail;
import shop.S5G.shop.repository.order.qdsl.OrderDetailQuerydslRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long>,
    OrderDetailQuerydslRepository {

    long countOrderDetailsByOrderId(long orderId);
}
