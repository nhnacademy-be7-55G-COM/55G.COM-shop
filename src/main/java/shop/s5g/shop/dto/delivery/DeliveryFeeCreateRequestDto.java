package shop.s5g.shop.dto.delivery;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record DeliveryFeeCreateRequestDto(
    @Min(0)
    long fee,
    @Min(0)
    long condition,
    @Min(0)
    int refundFee,
    @NotNull
    @Size(min=1, max = 20)
    String name
) {

}
