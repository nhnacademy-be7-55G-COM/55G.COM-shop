package shop.s5g.shop.dto.bookAuthor;

public record BookAuthorResponseDto(
    long authorId,
    String authorName,
    long authorTypeId,
    String typeName
) {

}
