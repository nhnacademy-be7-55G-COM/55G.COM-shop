package shop.S5G.shop.dto.order;

import shop.S5G.shop.entity.order.Order;

public record OrderCreateResponseDto(long orderId) {

    public static OrderCreateResponseDto of(Order order) {
        return new OrderCreateResponseDto(order.getId());
    }
}
