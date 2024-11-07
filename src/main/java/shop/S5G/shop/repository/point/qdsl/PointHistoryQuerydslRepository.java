package shop.S5G.shop.repository.point.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.S5G.shop.dto.point.PointHistoryResponseDto;

public interface PointHistoryQuerydslRepository {

    Page<PointHistoryResponseDto> findPointHistoryByMemberId(long memberId, Pageable pageable);
}
