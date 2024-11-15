package shop.s5g.shop.dto.book.status;

import jakarta.validation.constraints.NotNull;

public record BookStatusResponseDto(
        @NotNull
        String name
) {
}
