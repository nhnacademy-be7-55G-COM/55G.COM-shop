package shop.S5G.shop.service.point;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.PageResponseDto;
import shop.S5G.shop.dto.point.PointHistoryResponseDto;

public interface PointHistoryService {

    @Transactional(readOnly = true)
    PageResponseDto<PointHistoryResponseDto> getPointHistoryPage(long customerId,
        Pageable pageable);

    void deactivateHistory(long pointHistoryId);
}
