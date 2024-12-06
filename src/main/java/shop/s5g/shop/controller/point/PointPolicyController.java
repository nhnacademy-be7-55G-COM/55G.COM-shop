package shop.s5g.shop.controller.point;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.point.PointPolicyCreateRequestDto;
import shop.s5g.shop.dto.point.PointPolicyRemoveRequestDto;
import shop.s5g.shop.dto.point.PointPolicyResponseDto;
import shop.s5g.shop.dto.point.PointPolicyUpdateRequestDto;
import shop.s5g.shop.dto.point.PointPolicyView;
import shop.s5g.shop.exception.BadRequestException;
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

    @PostMapping("/update")
    public ResponseEntity<Void> updatePolicy(
        @RequestBody @Valid PointPolicyUpdateRequestDto pointPolicyUpdateRequestDto,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException();
        }
        pointPolicyService.updatePolicyValue(pointPolicyUpdateRequestDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createPolicy(
        @RequestBody @Valid PointPolicyCreateRequestDto pointPolicyCreateRequestDto,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException();
        }

        pointPolicyService.createPointPolicy(pointPolicyCreateRequestDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removePolicy(
        @RequestBody @Valid PointPolicyRemoveRequestDto pointPolicyRemoveRequestDto,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException();
        }
        pointPolicyService.removePointPolicy(pointPolicyRemoveRequestDto);

        return ResponseEntity.ok().build();
    }
}
