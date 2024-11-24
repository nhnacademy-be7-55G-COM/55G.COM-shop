package shop.s5g.shop.dto.refund;

import shop.s5g.shop.entity.delivery.Delivery;
import shop.s5g.shop.entity.order.OrderDetail;

public record OrderDetailDeliveryPairContainer(
    OrderDetail orderDetail,
    Delivery delivery
) {

}
