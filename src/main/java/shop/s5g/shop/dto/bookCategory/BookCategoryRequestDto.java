package shop.s5g.shop.dto.bookCategory;

import jakarta.validation.constraints.NotNull;

public record BookCategoryRequestDto (
    @NotNull
    Long categoryId,
    @NotNull
    Long BookId
){
}
