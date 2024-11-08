package shop.S5G.shop.service.point.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.PageResponseDto;
import shop.S5G.shop.dto.point.PointHistoryCreateRequestDto;
import shop.S5G.shop.dto.point.PointHistoryCreateResponseDto;
import shop.S5G.shop.dto.point.PointHistoryResponseDto;
import shop.S5G.shop.entity.member.Member;
import shop.S5G.shop.entity.point.PointHistory;
import shop.S5G.shop.entity.point.PointSource;
import shop.S5G.shop.exception.EssentialDataNotFoundException;
import shop.S5G.shop.exception.ResourceNotFoundException;
import shop.S5G.shop.exception.member.MemberNotFoundException;
import shop.S5G.shop.repository.member.MemberRepository;
import shop.S5G.shop.repository.point.PointHistoryRepository;
import shop.S5G.shop.repository.point.PointSourceRepository;
import shop.S5G.shop.service.point.PointHistoryService;

@Service
@RequiredArgsConstructor
@Transactional
public class PointHistoryServiceImpl implements PointHistoryService {
    private final PointHistoryRepository pointHistoryRepository;
    private final PointSourceRepository pointSourceRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Override
    public PageResponseDto<PointHistoryResponseDto> getPointHistoryPage(long memberId,
        Pageable pageable) {
        return PageResponseDto.of(pointHistoryRepository.findPointHistoryByMemberId(memberId, pageable));
    }

    @Override
    public void deactivateHistory(long pointHistoryId) {
        PointHistory pointHistory = pointHistoryRepository.findByIdAndActiveIsTrue(pointHistoryId).orElseThrow(
            () -> new ResourceNotFoundException("Point history does not exist")
        );
        pointHistory.setActive(false);
    }

    @Override
    public PointHistoryCreateResponseDto createPointHistory(long memberId, PointHistoryCreateRequestDto createRequest) {
        PointSource pointSource = pointSourceRepository.findBySourceName(createRequest.pointSourceName()).orElseThrow(
            () -> new EssentialDataNotFoundException("PointSource does not exist: " + createRequest.pointSourceName())
        );

        Member member = memberRepository.findByIdAndStatus_TypeName(memberId, MemberRepository.ACTIVE_STATUS).orElseThrow(
            () -> new MemberNotFoundException("Member does not exist or non active: " + memberId)
        );

        long offset = createRequest.pointOffset();
        PointHistory pointHistory = new PointHistory(pointSource, member, offset, member.getPoint()+offset);
        PointHistory saved = pointHistoryRepository.save(pointHistory);

        return new PointHistoryCreateResponseDto(saved.getId(), saved.getRemainingPoint());
    }
}
