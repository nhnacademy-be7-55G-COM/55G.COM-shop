package shop.S5G.shop.dto.bookstatus;

import jakarta.validation.constraints.NotNull;

public record BookStatusResponseDto(
        @NotNull
        String name
) {
}
