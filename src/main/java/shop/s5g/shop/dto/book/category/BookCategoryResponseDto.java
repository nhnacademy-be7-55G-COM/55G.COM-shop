package shop.s5g.shop.dto.book.category;

import jakarta.validation.constraints.NotNull;

public record BookCategoryResponseDto (
    @NotNull
    Long categoryId,
    @NotNull
    Long bookId
){
}
