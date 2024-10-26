package shop.S5G.shop.repository.order.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.S5G.shop.dto.order.OrderWithDetailResponseDto;

public interface OrderQuerydslRepository {

    Page<OrderWithDetailResponseDto> findOrdersByCustomerId(long id, Pageable pageable);
}
