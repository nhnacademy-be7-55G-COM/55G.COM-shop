package shop.S5G.shop.dto.point;

public record PointHistoryCreateRequestDto(
    String pointSourceName,
    long pointOffset
) {

}
