package shop.s5g.shop.repository.order.qdsl;

import java.time.LocalDate;
import java.util.List;
import shop.s5g.shop.dto.order.OrderWithDetailResponseDto;

public interface OrderQuerydslRepository {

    List<OrderWithDetailResponseDto> findOrdersByCustomerIdBetweenDates(long id, LocalDate fromDate,
        LocalDate toDate);
    List<OrderWithDetailResponseDto> findOrdersByCustomerId(long id);
}