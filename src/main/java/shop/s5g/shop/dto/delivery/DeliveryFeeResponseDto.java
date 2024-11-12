package shop.s5g.shop.dto.delivery;

import shop.s5g.shop.entity.delivery.Delivery;
import shop.s5g.shop.entity.delivery.DeliveryFee;

public record DeliveryFeeResponseDto(
    long id,
    long fee,
    long condition,
    int refundFee,
    String name
) {
    public static DeliveryFeeResponseDto of(DeliveryFee entity) {
        return new DeliveryFeeResponseDto(
            entity.getId(),
            entity.getFee(),
            entity.getCondition(),
            entity.getRefundFee(),
            entity.getName()
        );
    }
}
