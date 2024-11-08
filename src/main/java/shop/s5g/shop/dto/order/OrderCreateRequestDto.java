package shop.s5g.shop.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import shop.s5g.shop.dto.delivery.DeliveryCreateRequestDto;

// TODO: front 쪽 리팩토링
public record OrderCreateRequestDto(
    @NotNull
    DeliveryCreateRequestDto delivery,
    @Size(min = 1)
    List<OrderDetailCreateRequestDto> cartList,
    @Min(0)
    long netPrice,
    @Min(0)
    long totalPrice
) {}
