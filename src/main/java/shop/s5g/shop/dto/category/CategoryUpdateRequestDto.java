package shop.s5g.shop.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryUpdateRequestDto(
        @NotNull
        String categoryName
) {
}