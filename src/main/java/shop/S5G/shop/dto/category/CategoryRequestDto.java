package shop.S5G.shop.dto.category;

import lombok.Getter;
import lombok.Setter;
import shop.S5G.shop.entity.Category;

@Getter @Setter
public class CategoryRequestDto {
    private Long categoryId;
    private Category parentCategory;
    private String categoryName;
    private boolean active;

    public CategoryRequestDto(Long id, Category parentCategory, String categoryName, boolean active) {
        this.categoryId = id;
        this.parentCategory = parentCategory;
        this.categoryName = categoryName;
        this.active = active;
    }
}