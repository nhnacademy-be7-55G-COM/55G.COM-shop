package shop.S5G.shop.dto.bookcategory;

import jakarta.validation.constraints.NotNull;

public record BookCategoryRequestDto (
    @NotNull
    Long categoryId,
    @NotNull
    Long BookId
){
}
