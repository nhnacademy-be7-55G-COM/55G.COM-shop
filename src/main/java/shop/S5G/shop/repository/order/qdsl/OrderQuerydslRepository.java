package shop.S5G.shop.repository.order.qdsl;

import java.time.LocalDate;
import java.util.List;
import shop.S5G.shop.dto.order.OrderWithDetailResponseDto;

public interface OrderQuerydslRepository {

    List<OrderWithDetailResponseDto> findOrdersByCustomerIdBetweenDates(long id, LocalDate fromDate,
        LocalDate toDate);
    List<OrderWithDetailResponseDto> findOrdersByCustomerId(long id);
}
