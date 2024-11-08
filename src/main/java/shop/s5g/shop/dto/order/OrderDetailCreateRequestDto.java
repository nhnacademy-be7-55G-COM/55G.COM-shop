package shop.s5g.shop.dto.order;

import jakarta.validation.constraints.Min;

public record OrderDetailCreateRequestDto(
    @Min(1)
    long bookId,
    @Min(1)
    long wrappingPaperId,
    @Min(1)
    int quantity,
    @Min(0)
    long totalPrice,
    @Min(0)
    int accumulationPrice
) {}
