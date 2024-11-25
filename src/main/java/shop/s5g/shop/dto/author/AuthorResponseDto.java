package shop.s5g.shop.dto.author;

public record AuthorResponseDto(
    long authorId,
    String name,
    boolean active
) {

}
