package shop.s5g.shop.dto.bookCategory;

import jakarta.validation.constraints.NotNull;

public record BookCategoryBookResponseDto(
        @NotNull
        Long categoryId,
        @NotNull
        Long bookId
) {
}
