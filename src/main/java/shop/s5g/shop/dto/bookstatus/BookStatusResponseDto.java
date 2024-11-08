package shop.s5g.shop.dto.bookstatus;

import jakarta.validation.constraints.NotNull;

public record BookStatusResponseDto(
        @NotNull
        String name
) {
}
