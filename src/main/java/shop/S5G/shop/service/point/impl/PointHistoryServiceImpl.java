package shop.S5G.shop.service.point.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.PageResponseDto;
import shop.S5G.shop.dto.point.PointHistoryResponseDto;
import shop.S5G.shop.entity.point.PointHistory;
import shop.S5G.shop.exception.ResourceNotFoundException;
import shop.S5G.shop.repository.point.PointHistoryRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class PointHistoryServiceImpl implements shop.S5G.shop.service.point.PointHistoryService {
    private final PointHistoryRepository pointHistoryRepository;

    @Transactional(readOnly = true)
    @Override
    public PageResponseDto<PointHistoryResponseDto> getPointHistoryPage(long customerId,
        Pageable pageable) {
        return PageResponseDto.of(pointHistoryRepository.findPointHistoryByCustomerId(customerId, pageable));
    }

    @Override
    public void deactivateHistory(long pointHistoryId) {
        PointHistory pointHistory = pointHistoryRepository.findById(pointHistoryId).orElseThrow(
            () -> new ResourceNotFoundException("Point history does not exist")
        );
        pointHistory.setActive(false);
    }
}
