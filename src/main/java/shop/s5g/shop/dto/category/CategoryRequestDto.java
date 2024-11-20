package shop.s5g.shop.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryRequestDto (

    String categoryName,
    @NotNull
    Long parentCategoryId
){
}