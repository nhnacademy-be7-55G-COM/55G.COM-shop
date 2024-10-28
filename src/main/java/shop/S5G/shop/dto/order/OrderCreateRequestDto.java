package shop.S5G.shop.dto.order;

import jakarta.validation.constraints.Min;
import shop.S5G.shop.dto.delivery.DeliveryCreateRequestDto;

public record OrderCreateRequestDto(
    @Min(1)
    long customerId,
    DeliveryCreateRequestDto delivery,
    OrderDetailCreateRequestDto[] cartList,
    @Min(0)
    long netPrice,
    @Min(0)
    long totalPrice
) {}
