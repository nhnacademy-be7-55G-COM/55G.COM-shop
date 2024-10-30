package shop.S5G.shop.dto.category;

import jakarta.validation.constraints.NotNull;
import shop.S5G.shop.entity.Category;

public record CategoryResponseDto (
    Category parentCategory,
    @NotNull
    String categoryName,
    @NotNull
    boolean active
){
}
