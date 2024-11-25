package shop.s5g.shop.service.member.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.dto.member_status.MemberStatusRequestDto;
import shop.s5g.shop.dto.member_status.MemberStatusResponseDto;
import shop.s5g.shop.entity.member.MemberStatus;
import shop.s5g.shop.exception.member.MemberStatusAlreadyExistsException;
import shop.s5g.shop.exception.member.MemberStatusNotFoundException;
import shop.s5g.shop.repository.member.MemberStatusRepository;

@ExtendWith(MockitoExtension.class)
class MemberStatusServiceImplTest {

    @Mock
    private MemberStatusRepository memberStatusRepository;

    @InjectMocks
    private MemberStatusServiceImpl memberStatusService;

    private MemberStatus testMemberStatus;

    @BeforeEach
    void setUp() {
        testMemberStatus = new MemberStatus(1L, "ACTIVE");
    }

    @Test
    void saveMemberStatusTest() {
        // Given
        MemberStatusRequestDto requestDto = new MemberStatusRequestDto("ACTIVE");
        when(memberStatusRepository.existsByTypeName(requestDto.typeName())).thenReturn(false);

        // When
        memberStatusService.saveMemberStatus(requestDto);

        // Then
        verify(memberStatusRepository, times(1)).save(any(MemberStatus.class));
    }

    @Test
    void saveMemberStatus_AlreadyExistsTest() {
        // Given
        MemberStatusRequestDto requestDto = new MemberStatusRequestDto("ACTIVE");
        when(memberStatusRepository.existsByTypeName(requestDto.typeName())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> memberStatusService.saveMemberStatus(requestDto))
            .isInstanceOf(MemberStatusAlreadyExistsException.class)
            .hasMessage("ACTIVE 등록하려는 상태가 이미 존재합니다");
        verify(memberStatusRepository, never()).save(any(MemberStatus.class));
    }

    @Test
    void updateMemberStatusTest() {
        // Given
        long memberStatusId = testMemberStatus.getMemberStatusId();
        MemberStatusRequestDto requestDto = new MemberStatusRequestDto("UPDATED_STATUS");
        when(memberStatusRepository.existsById(memberStatusId)).thenReturn(true);

        // When
        memberStatusService.updateMemberStatus(memberStatusId, requestDto);

        // Then
        verify(memberStatusRepository, times(1)).updateMemberStatus(memberStatusId, requestDto);
    }

    @Test
    void updateMemberStatus_NotFoundTest() {
        // Given
        long nonExistentId = 999L;
        MemberStatusRequestDto requestDto = new MemberStatusRequestDto("UPDATED_STATUS");
        when(memberStatusRepository.existsById(nonExistentId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> memberStatusService.updateMemberStatus(nonExistentId, requestDto))
            .isInstanceOf(MemberStatusNotFoundException.class)
            .hasMessage("변경하려는 상태가 존재하지 않습니다");
        verify(memberStatusRepository, never()).updateMemberStatus(anyLong(), any());
    }

    @Test
    void getMemberStatusTest() {
        // Given
        long memberStatusId = testMemberStatus.getMemberStatusId();
        when(memberStatusRepository.findById(memberStatusId)).thenReturn(
            Optional.of(testMemberStatus));

        // When
        MemberStatusResponseDto responseDto = memberStatusService.getMemberStatus(memberStatusId);

        // Then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.memberStatusId()).isEqualTo(testMemberStatus.getMemberStatusId());
        assertThat(responseDto.typeName()).isEqualTo(testMemberStatus.getTypeName());
        verify(memberStatusRepository, times(1)).findById(memberStatusId);
    }

    @Test
    void getMemberStatus_NotFoundTest() {
        // Given
        long nonExistentId = 999L;
        when(memberStatusRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> memberStatusService.getMemberStatus(nonExistentId))
            .isInstanceOf(MemberStatusNotFoundException.class)
            .hasMessage("조회하려는 상태가 존재하지 않습니다");
        verify(memberStatusRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void getAllMemberStatusTest() {
        // Given
        List<MemberStatus> statuses = List.of(
            new MemberStatus("ACTIVE"),
            new MemberStatus("INACTIVE")
        );
        when(memberStatusRepository.findAll()).thenReturn(statuses);

        // When
        List<MemberStatusResponseDto> allStatuses = memberStatusService.getAllMemberStatus();

        // Then
        assertThat(allStatuses).hasSize(2);
        assertThat(allStatuses.get(0).typeName()).isEqualTo("ACTIVE");
        assertThat(allStatuses.get(1).typeName()).isEqualTo("INACTIVE");
        verify(memberStatusRepository, times(1)).findAll();
    }

    @Test
    void deleteMemberStatusTest() {
        // Given
        long memberStatusId = testMemberStatus.getMemberStatusId();
        when(memberStatusRepository.existsById(memberStatusId)).thenReturn(true);

        // When
        memberStatusService.deleteMemberStatus(memberStatusId);

        // Then
        verify(memberStatusRepository, times(1)).deleteById(memberStatusId);
    }

    @Test
    void deleteMemberStatus_NotFoundTest() {
        // Given
        long nonExistentId = 999L;
        when(memberStatusRepository.existsById(nonExistentId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> memberStatusService.deleteMemberStatus(nonExistentId))
            .isInstanceOf(MemberStatusNotFoundException.class)
            .hasMessage("삭제하려는 상태가 존재하지 않습니다");
        verify(memberStatusRepository, never()).deleteById(anyLong());
    }

    @Test
    void getMemberStatusByTypeNameTest() {
        // Given
        String typeName = "ACTIVE";
        when(memberStatusRepository.existsByTypeName(typeName)).thenReturn(true);
        when(memberStatusRepository.findByTypeName(typeName)).thenReturn(testMemberStatus);

        // When
        MemberStatus memberStatus = memberStatusService.getMemberStatusByTypeName(typeName);

        // Then
        assertThat(memberStatus).isNotNull();
        assertThat(memberStatus.getTypeName()).isEqualTo("ACTIVE");
        verify(memberStatusRepository, times(1)).findByTypeName(typeName);
    }

    @Test
    void getMemberStatusByTypeName_NotFoundTest() {
        // Given
        String nonExistentTypeName = "NON_EXISTENT";
        when(memberStatusRepository.existsByTypeName(nonExistentTypeName)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> memberStatusService.getMemberStatusByTypeName(nonExistentTypeName))
            .isInstanceOf(MemberStatusAlreadyExistsException.class)
            .hasMessage(nonExistentTypeName + " 존재하지 않습니다");
        verify(memberStatusRepository, never()).findByTypeName(nonExistentTypeName);
    }

}
