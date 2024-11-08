package shop.s5g.shop.dto.point;

public record PointHistoryCreateRequestDto(
    String pointSourceName,
    long pointOffset
) {

}
