package shop.s5g.shop.service.point;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.point.PointHistoryCreateRequestDto;
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
import shop.s5g.shop.service.point.impl.PointHistoryServiceImpl;

@ExtendWith(MockitoExtension.class)
class PointHistoryServiceImplTest {
    @Mock
    PointHistoryRepository pointHistoryRepository;
    @Mock
    PointSourceRepository pointSourceRepository;
    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    PointHistoryServiceImpl service;

    @Test
    void getHistoryPageTest() {
        Pageable pageable = Pageable.ofSize(10);
        PageImpl<PointHistoryResponseDto> pageImpl = new PageImpl<>(List.of(), pageable, 0);
        when(pointHistoryRepository.findPointHistoryByMemberId(anyLong(), any())).thenReturn(pageImpl);

        assertThatCode(() -> service.getPointHistoryPage(1L, pageable)).doesNotThrowAnyException();

        verify(pointHistoryRepository, times(1)).findPointHistoryByMemberId(eq(1L), eq(pageable));
    }

    @Test
    void deactivateHistoryFailTest() {
        when(pointHistoryRepository.findByIdAndActiveIsTrue(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deactivateHistory(1L))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(pointHistoryRepository, times(1)).findByIdAndActiveIsTrue(1L);
    }

    @Test
    void deactivateHistorySuccessTest() {
        PointHistory pointHistory = mock(PointHistory.class);
        when(pointHistoryRepository.findByIdAndActiveIsTrue(anyLong())).thenReturn(Optional.of(pointHistory));

        assertThatCode(() -> service.deactivateHistory(1L))
            .doesNotThrowAnyException();

        verify(pointHistoryRepository, times(1)).findByIdAndActiveIsTrue(1L);
        verify(pointHistory, times(1)).setActive(false);
    }
    PointHistoryCreateRequestDto createRequest = new PointHistoryCreateRequestDto(
        "테스트 소스", 100
    );

    @Test
    void createPointHistoryPointSourceNotAvailableTest() {
        when(pointSourceRepository.findBySourceName(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createPointHistory(1L, createRequest))
            .isInstanceOf(EssentialDataNotFoundException.class);

        verify(pointSourceRepository, times(1)).findBySourceName(anyString());
        verify(pointHistoryRepository, never()).save(any());
    }
    @Test
    void createPointHistoryMemberNotFoundTest() {
        PointSource pointSource = mock(PointSource.class);
        when(pointSourceRepository.findBySourceName(anyString())).thenReturn(Optional.of(pointSource));
        when(memberRepository.findByIdAndStatus_TypeName(anyLong(), eq(MemberRepository.ACTIVE_STATUS))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createPointHistory(1L, createRequest))
            .isInstanceOf(MemberNotFoundException.class);

        verify(pointSourceRepository, times(1)).findBySourceName(anyString());
        verify(memberRepository, times(1)).findByIdAndStatus_TypeName(anyLong(), eq(MemberRepository.ACTIVE_STATUS));
        verify(pointHistoryRepository, never()).save(any());
    }
    @Test
    void createPointHistorySuccessTest() {
        PointSource pointSource = mock(PointSource.class);
        Member member = mock(Member.class);
        PointHistory saved = new PointHistory(1L, pointSource, member, 5200L, 5200L, LocalDateTime.now(), true);
        when(pointSourceRepository.findBySourceName(anyString())).thenReturn(Optional.of(pointSource));
        when(memberRepository.findByIdAndStatus_TypeName(anyLong(), eq(MemberRepository.ACTIVE_STATUS))).thenReturn(Optional.of(member));
        when(member.getPoint()).thenReturn(5000L);
        when(pointHistoryRepository.save(any())).thenReturn(saved);

        assertThatCode(() -> service.createPointHistory(1L, createRequest))
            .doesNotThrowAnyException();

        verify(pointSourceRepository, times(1)).findBySourceName(anyString());
        verify(memberRepository, times(1)).findByIdAndStatus_TypeName(anyLong(), eq(MemberRepository.ACTIVE_STATUS));
        verify(member, times(1)).setPoint(anyLong());
        verify(pointHistoryRepository, times(1)).save(any());
    }
}
