package shop.S5G.shop.repository.order.qdsl;

import java.util.List;
import shop.S5G.shop.dto.order.OrderDetailWithBookResponseDto;

public interface OrderDetailQuerydslRepository {

    List<OrderDetailWithBookResponseDto> queryAllDetailsByOrderId(long orderId);
}
