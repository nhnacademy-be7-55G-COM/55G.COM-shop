package shop.S5G.shop.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryUpdateRequestDto(
        @NotNull
        String categoryName,
        @NotNull
        boolean active
) {
}