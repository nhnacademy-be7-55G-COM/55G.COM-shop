package shop.S5G.shop.service.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.S5G.shop.dto.order.OrderWithDetailResponseDto;
import shop.S5G.shop.entity.order.Order;

public interface OrderService {

    Page<Order> findAllByCustomerId(long customerId, Pageable pageable);

    Page<OrderWithDetailResponseDto> queryAllOrdersByCustomerId(long customerId, Pageable pageable);
}