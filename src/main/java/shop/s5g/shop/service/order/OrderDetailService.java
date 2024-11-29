package shop.s5g.shop.service.order;

import java.util.List;
import shop.s5g.shop.dto.order.OrderDetailInfoDto;
import shop.s5g.shop.dto.order.OrderDetailWithBookResponseDto;

public interface OrderDetailService {

    List<OrderDetailWithBookResponseDto> getOrderDetailsWithBook(long orderId);

    //    @Transactional(readOnly = true)
    List<OrderDetailWithBookResponseDto> getOrderDetailsWithBook(String uuid);

    void changeOrderDetailType(long detailId, String type);

    OrderDetailInfoDto getOrderDetailInfo(String uuid);

//    void cancelOrderDetail(long orderDetailId);
}
