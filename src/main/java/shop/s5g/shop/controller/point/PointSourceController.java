package shop.s5g.shop.controller.point;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.point.PointPolicyFormResponseDto;
import shop.s5g.shop.dto.point.PointSourceCreateRequestDto;
import shop.s5g.shop.entity.point.PointSource;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.service.point.PointSourceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/point/source")
public class PointSourceController {

    private final PointSourceService pointSourceService;

    @GetMapping("/create")
    public ResponseEntity<List<PointPolicyFormResponseDto>> getPointPolicyFormData() {

        List<PointPolicyFormResponseDto> responseList = pointSourceService.getPointPolicyFormData();

        return ResponseEntity.ok().body(responseList);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createPointSource(
        @RequestBody @Valid PointSourceCreateRequestDto pointSourceCreateRequestDto,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException();
        }

        pointSourceService.createPointSource(pointSourceCreateRequestDto);

        return ResponseEntity.ok().build();
    }

}
