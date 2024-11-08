package shop.s5g.shop.repository.order.qdsl;

import java.util.List;
import shop.s5g.shop.dto.order.OrderDetailWithBookResponseDto;

public interface OrderDetailQuerydslRepository {

    List<OrderDetailWithBookResponseDto> queryAllDetailsByOrderId(long orderId);
}
