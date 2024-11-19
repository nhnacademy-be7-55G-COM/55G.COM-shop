package shop.s5g.shop.repository.order.qdsl;

import java.time.LocalDate;
import java.util.List;
import shop.s5g.shop.dto.order.OrderAdminTableView;
import shop.s5g.shop.dto.order.OrderQueryFilterDto;
import shop.s5g.shop.dto.order.OrderWithDetailResponseDto;
import shop.s5g.shop.entity.order.Order;

public interface OrderQuerydslRepository {

    List<OrderWithDetailResponseDto> findOrdersByCustomerIdBetweenDates(long id, LocalDate fromDate,
        LocalDate toDate);
    List<OrderWithDetailResponseDto> findOrdersByCustomerId(long id);

    Order findOrderByIdFetch(long orderId);

    List<OrderAdminTableView> findOrdersByCustomerIdForAdmin(long id);

    List<OrderAdminTableView> findOrdersUsingFilterForAdmin(OrderQueryFilterDto filter);
}
