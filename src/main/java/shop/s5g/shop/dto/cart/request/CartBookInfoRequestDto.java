package shop.s5g.shop.dto.cart.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartBookInfoRequestDto(
    @NotNull
    Long bookId,

    @NotNull
    @Min(1)
    Integer quantity
) {}
