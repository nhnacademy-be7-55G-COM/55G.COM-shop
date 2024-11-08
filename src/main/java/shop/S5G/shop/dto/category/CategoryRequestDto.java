package shop.S5G.shop.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import shop.S5G.shop.entity.Category;

public record CategoryRequestDto (

    String parentCategoryName,
    @NotNull
    String categoryName,
    @NotNull
    boolean active
){
}