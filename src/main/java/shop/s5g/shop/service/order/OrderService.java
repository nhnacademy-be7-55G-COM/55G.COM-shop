package shop.s5g.shop.service.order;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.order.OrderAdminTableView;
import shop.s5g.shop.dto.order.OrderCreateRequestDto;
import shop.s5g.shop.dto.order.OrderCreateResponseDto;
import shop.s5g.shop.dto.order.OrderQueryFilterDto;
import shop.s5g.shop.dto.order.OrderQueryRequestDto;
import shop.s5g.shop.dto.order.OrderWithDetailResponseDto;
import shop.s5g.shop.entity.order.Order;

public interface OrderService {

    Page<Order> findAllByCustomerId(long customerId, Pageable pageable);

    List<OrderWithDetailResponseDto> getAllOrdersWithDetail(long customerId);

    OrderCreateResponseDto createOrder(long customerId, OrderCreateRequestDto requestDto);

    @Transactional(readOnly = true)
    List<OrderWithDetailResponseDto> getAllOrdersBetweenDates(
        long customerId, OrderQueryRequestDto queryRequest);

    void deactivateOrder(long orderId);

    List<OrderAdminTableView> getOrderListAdmin(OrderQueryFilterDto filter);
}
