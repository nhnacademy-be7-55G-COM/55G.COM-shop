package shop.s5g.shop.repository.order.qdsl;

import java.util.List;
import shop.s5g.shop.dto.order.OrderDetailWithBookResponseDto;
import shop.s5g.shop.entity.order.OrderDetail;

public interface OrderDetailQuerydslRepository {

    List<OrderDetailWithBookResponseDto> queryAllDetailsByOrderId(long orderId);

    List<OrderDetail> fetchOrderDetailsByOrderId(long orderId);
}
