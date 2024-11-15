package shop.s5g.shop.service.point;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.point.PointHistoryCreateRequestDto;
import shop.s5g.shop.dto.point.PointHistoryCreateResponseDto;
import shop.s5g.shop.dto.point.PointHistoryResponseDto;

public interface PointHistoryService {

    @Transactional(readOnly = true)
    PageResponseDto<PointHistoryResponseDto> getPointHistoryPage(long memberId,
        Pageable pageable);

    void deactivateHistory(long pointHistoryId);

    PointHistoryCreateResponseDto createPointHistory(long memberId,
        PointHistoryCreateRequestDto createRequest);
}
