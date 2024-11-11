package shop.s5g.shop.dto.order;

import shop.s5g.shop.entity.order.Order;

public record OrderCreateResponseDto(long orderId) {

    public static OrderCreateResponseDto of(Order order) {
        return new OrderCreateResponseDto(order.getId());
    }
}