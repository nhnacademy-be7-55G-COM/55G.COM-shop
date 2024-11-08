package shop.s5g.shop.dto.point;

import shop.s5g.shop.entity.point.PointSource;
import shop.s5g.shop.entity.point.PointSource.Name;

public record PointHistoryCreateRequestDto(
    String pointSourceName,
    long pointOffset
) {
    public static final PointHistoryCreateRequestDto REGISTER_POINT =
            new PointHistoryCreateRequestDto(Name.REGISTER.getDataName(), 5000L);
}
