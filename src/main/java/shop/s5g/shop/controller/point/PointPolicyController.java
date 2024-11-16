package shop.s5g.shop.controller.point;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.point.PointPolicyResponseDto;
import shop.s5g.shop.dto.point.PointPolicyView;
import shop.s5g.shop.service.point.PointPolicyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/point/policies")
public class PointPolicyController {
    private final PointPolicyService pointPolicyService;
    private static final String PURCHASE = "구매";

    @GetMapping
    public List<PointPolicyResponseDto> getPolicies() {
        return pointPolicyService.getAllPolicies();
    }

    @GetMapping("/purchase")
    public PointPolicyView getPurchasePointPolicy() {
        return pointPolicyService.getPolicy(PURCHASE);
    }
}
