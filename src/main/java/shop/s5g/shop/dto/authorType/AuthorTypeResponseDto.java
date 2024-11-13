package shop.s5g.shop.dto.authorType;

public record AuthorTypeResponseDto(
    long authorTypeId,
    String typeName,
    boolean active
) {

}
