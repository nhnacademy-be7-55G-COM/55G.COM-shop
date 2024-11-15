package shop.s5g.shop.dto.bookCategory;

import jakarta.validation.constraints.NotNull;

public record BookCategoryResponseDto (
    @NotNull
    Long categoryId,
    @NotNull
    Long bookId
){
}
