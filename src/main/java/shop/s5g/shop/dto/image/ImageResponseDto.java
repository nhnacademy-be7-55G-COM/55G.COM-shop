package shop.s5g.shop.dto.image;

public record ImageResponseDto(
    String id,
    String url,
    String path,
    long bytes
) {

}