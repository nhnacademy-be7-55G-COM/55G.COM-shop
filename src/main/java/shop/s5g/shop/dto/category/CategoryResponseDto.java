package shop.s5g.shop.dto.category;

import jakarta.validation.constraints.NotNull;

public record CategoryResponseDto (
    String parentCategoryName,
    @NotNull
    String categoryName,
    @NotNull
    boolean active
){
}
