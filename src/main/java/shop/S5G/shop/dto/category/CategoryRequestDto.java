package shop.S5G.shop.dto.category;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CategoryRequestDto {
    private Long parentCategory;
    private String categoryName;
    private boolean active;
}