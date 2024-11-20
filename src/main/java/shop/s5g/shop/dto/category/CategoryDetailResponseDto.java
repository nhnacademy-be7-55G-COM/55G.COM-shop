package shop.s5g.shop.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryDetailResponseDto(
        @NotNull
        long categoryId,
        long parentCategoryId,
        @NotNull
        String categoryName
) {
}
