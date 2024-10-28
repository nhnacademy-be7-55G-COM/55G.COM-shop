package shop.S5G.shop.dto.category;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CategoryUpdateInBookResponseDto {
    private String categoryName;

    public CategoryUpdateInBookResponseDto() {
    }

    public CategoryUpdateInBookResponseDto(String categoryName) {
        this.categoryName = categoryName;
    }
}
