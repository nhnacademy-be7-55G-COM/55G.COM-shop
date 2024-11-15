package shop.s5g.shop.repository.point.qdsl;

import java.util.List;
import shop.s5g.shop.dto.point.PointPolicyResponseDto;

public interface PointPolicyQuerydslRepository {

    List<PointPolicyResponseDto> findAllPolicies();
}
