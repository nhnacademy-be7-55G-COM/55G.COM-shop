package shop.S5G.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.dto.PageResponseDto;
import shop.S5G.shop.dto.point.PointHistoryResponseDto;
import shop.S5G.shop.security.ShopMemberDetail;
import shop.S5G.shop.service.point.PointHistoryService;

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

}
