package shop.s5g.shop.dto.bookStatus;

import jakarta.validation.constraints.NotNull;

public record BookStatusResponseDto(
        @NotNull
        String name
) {
}
