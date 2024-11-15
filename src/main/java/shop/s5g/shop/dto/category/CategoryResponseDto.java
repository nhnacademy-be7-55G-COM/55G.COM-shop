package shop.s5g.shop.dto.category;

public record CategoryResponseDto(

        long categoryId,
        long parentCategoryId,
        String categoryName,
        boolean active
) {
}
