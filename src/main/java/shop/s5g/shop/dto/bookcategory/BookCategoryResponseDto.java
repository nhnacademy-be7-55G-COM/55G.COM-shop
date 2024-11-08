package shop.s5g.shop.dto.bookcategory;

import jakarta.validation.constraints.NotNull;

public record BookCategoryResponseDto (
    @NotNull
    Long categoryId,
    @NotNull
    Long bookId
){
}
