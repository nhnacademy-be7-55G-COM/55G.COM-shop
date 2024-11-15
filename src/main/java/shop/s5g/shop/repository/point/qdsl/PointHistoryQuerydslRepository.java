package shop.s5g.shop.repository.point.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.point.PointHistoryResponseDto;

public interface PointHistoryQuerydslRepository {

    Page<PointHistoryResponseDto> findPointHistoryByMemberId(long memberId, Pageable pageable);
}
