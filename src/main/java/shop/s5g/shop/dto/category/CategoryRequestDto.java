package shop.s5g.shop.dto.category;

public record CategoryRequestDto (

    String categoryName,
    Long parentCategoryId
){
}