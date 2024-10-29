package shop.S5G.shop.service.order;

import java.util.List;
import shop.S5G.shop.dto.order.OrderDetailWithBookResponseDto;

public interface OrderDetailService {

    List<OrderDetailWithBookResponseDto> findOrderDetailsByOrderId(long orderId);
}
