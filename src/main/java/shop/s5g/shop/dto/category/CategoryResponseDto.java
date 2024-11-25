package shop.s5g.shop.dto.category;

import jakarta.annotation.Nullable;

public record CategoryResponseDto(

        Long categoryId,

        @Nullable
        Long parentCategoryId,

        String categoryName,

        boolean active
) {
}
