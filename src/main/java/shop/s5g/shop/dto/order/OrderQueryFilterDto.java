package shop.s5g.shop.dto.order;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;

public record OrderQueryFilterDto(
    @Min(1)
    @Nullable
    Long orderId,
    @Min(1)
    @Nullable
    Long customerId,
    @Nullable
    Boolean active,
    @Min(1)
    @Nullable
    Integer page,
    @Min(1)
    @Nullable
    Integer size,
    @Nullable
    String deliveryStatus
) {

}
