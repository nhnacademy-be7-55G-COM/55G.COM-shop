package shop.s5g.shop.service.order;

import java.util.List;
import shop.s5g.shop.dto.order.OrderDetailWithBookResponseDto;

public interface OrderDetailService {

    List<OrderDetailWithBookResponseDto> getOrderDetailsWithBook(long orderId);

    void changeOrderDetailType(long detailId, String type);

//    void cancelOrderDetail(long orderDetailId);
}
