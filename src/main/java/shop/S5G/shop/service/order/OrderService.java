package shop.S5G.shop.service.order;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.order.OrderCreateRequestDto;
import shop.S5G.shop.dto.order.OrderCreateResponseDto;
import shop.S5G.shop.dto.order.OrderQueryRequestDto;
import shop.S5G.shop.dto.order.OrderWithDetailResponseDto;
import shop.S5G.shop.entity.order.Order;

public interface OrderService {

    Page<Order> findAllByCustomerId(long customerId, Pageable pageable);

    List<OrderWithDetailResponseDto> getAllOrdersWithDetail(long customerId);

    OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto);

    @Transactional(readOnly = true)
    List<OrderWithDetailResponseDto> getAllOrdersBetweenDates(
        OrderQueryRequestDto queryRequest);
}
