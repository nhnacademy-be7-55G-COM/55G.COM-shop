package shop.S5G.shop.dto.category;

import lombok.Getter;
import lombok.Setter;
import shop.S5G.shop.entity.Category;

@Getter @Setter
public class CategoryResponseDto {
    private Category parentCategory;
    private String categoryName;
    private boolean active;
}
