package shop.s5g.shop.controller.point;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.point.PointHistoryCreateRequestDto;
import shop.s5g.shop.dto.point.PointHistoryCreateResponseDto;
import shop.s5g.shop.dto.point.PointHistoryResponseDto;
import shop.s5g.shop.security.ShopMemberDetail;
import shop.s5g.shop.service.point.PointHistoryService;

@RestController
@RequestMapping("/api/shop/point")
@RequiredArgsConstructor
public class PointController {
    private final PointHistoryService pointHistoryService;

    @GetMapping("/history")
    public PageResponseDto<PointHistoryResponseDto> fetchPointHistory(
        @AuthenticationPrincipal ShopMemberDetail memberDetail,
        Pageable pageable
    ) {
        return pointHistoryService.getPointHistoryPage(memberDetail.getCustomerId(), pageable);
    }

    @PutMapping
    public PointHistoryCreateResponseDto updatePoint(
        @AuthenticationPrincipal ShopMemberDetail memberDetail,
        @Valid @RequestBody PointHistoryCreateRequestDto usePoint
    ) {
        return pointHistoryService.createPointHistory(memberDetail.getCustomerId(), usePoint);
    }
}