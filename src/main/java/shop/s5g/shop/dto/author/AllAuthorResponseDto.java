package shop.s5g.shop.dto.author;

public record AllAuthorResponseDto(
        long authorId,
        String name,
        boolean active
) {
}
