package shop.s5g.shop.dto.author.type;

public record AuthorTypeResponseDto(
    long authorTypeId,
    String typeName,
    boolean active
) {

}
