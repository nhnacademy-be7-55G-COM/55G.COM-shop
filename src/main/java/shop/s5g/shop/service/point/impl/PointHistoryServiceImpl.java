package shop.s5g.shop.service.point.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.point.PointHistoryCreateRequestDto;
import shop.s5g.shop.dto.point.PointHistoryCreateResponseDto;
import shop.s5g.shop.dto.point.PointHistoryResponseDto;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.entity.point.PointHistory;
import shop.s5g.shop.entity.point.PointSource;
import shop.s5g.shop.exception.EssentialDataNotFoundException;
import shop.s5g.shop.exception.ResourceNotFoundException;
import shop.s5g.shop.exception.member.MemberNotFoundException;
import shop.s5g.shop.repository.member.MemberRepository;
import shop.s5g.shop.repository.point.PointHistoryRepository;
import shop.s5g.shop.repository.point.PointSourceRepository;
import shop.s5g.shop.service.point.PointHistoryService;

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
        long result = member.getPoint() + offset;
        PointHistory pointHistory = new PointHistory(pointSource, member, offset, result);
        member.setPoint(result);
        PointHistory saved = pointHistoryRepository.save(pointHistory);

        return new PointHistoryCreateResponseDto(saved.getId(), saved.getRemainingPoint());
    }
}
