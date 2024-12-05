package shop.s5g.shop.service.point;

import java.util.List;
import shop.s5g.shop.dto.point.PointPolicyFormResponseDto;
import shop.s5g.shop.dto.point.PointSourceCreateRequestDto;

public interface PointSourceService {
    void createPointSource(PointSourceCreateRequestDto pointSourceCreateRequestDto);

    List<PointPolicyFormResponseDto> getPointPolicyFormData();

}
