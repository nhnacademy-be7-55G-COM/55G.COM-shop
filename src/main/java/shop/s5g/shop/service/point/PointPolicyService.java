package shop.s5g.shop.service.point;

import java.util.List;
import shop.s5g.shop.dto.point.PointPolicyResponseDto;
import shop.s5g.shop.dto.point.PointPolicyView;

public interface PointPolicyService {

    List<PointPolicyResponseDto> getAllPolicies();

    PointPolicyView getPolicy(String name);
}
