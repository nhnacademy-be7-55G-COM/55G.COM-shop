package shop.s5g.shop.dto.category;

public record CategoryResponseDto(

        Long categoryId,
        Long parentCategoryId,
        String categoryName,
        boolean active
) {
}
