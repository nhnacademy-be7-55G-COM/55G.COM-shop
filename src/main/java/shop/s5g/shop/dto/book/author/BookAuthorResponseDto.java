package shop.s5g.shop.dto.book.author;

public record BookAuthorResponseDto(
    long authorId,
    String authorName,
    long authorTypeId,
    String typeName
) {

}
