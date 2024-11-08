package shop.s5g.shop.service.member.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
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
import shop.s5g.shop.dto.memberGrade.MemberGradeRequestDto;
import shop.s5g.shop.dto.memberGrade.MemberGradeResponseDto;
import shop.s5g.shop.entity.member.MemberGrade;
import shop.s5g.shop.exception.member.MemberGradeAlreadyExistsException;
import shop.s5g.shop.exception.member.MemberGradeNotFoundException;
import shop.s5g.shop.repository.member.MemberGradeRepository;

@ExtendWith(MockitoExtension.class)
class MemberGradeServiceImplTest {

    @Mock
    private MemberGradeRepository memberGradeRepository;

    @InjectMocks
    private MemberGradeServiceImpl memberGradeService;

    private MemberGrade testGrade;

    @BeforeEach
    void setUp() {
        testGrade = new MemberGrade("Gold", 5000, 1000, true);
    }

    @Test
    void addGrade_Success() {
        // Given
        MemberGradeRequestDto requestDto = new MemberGradeRequestDto("Gold", 5000, 1000);
        when(memberGradeRepository.existsByGradeNameAndActive("Gold", true)).thenReturn(false);

        // When
        memberGradeService.addGrade(requestDto);

        // Then
        verify(memberGradeRepository, times(1)).save(any(MemberGrade.class));
    }

    @Test
    void addGrade_AlreadyExists() {
        // Given
        MemberGradeRequestDto requestDto = new MemberGradeRequestDto("Gold", 5000, 1000);
        when(memberGradeRepository.existsByGradeNameAndActive("Gold", true)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> memberGradeService.addGrade(requestDto))
            .isInstanceOf(MemberGradeAlreadyExistsException.class)
            .hasMessage("Gold이 이미 존재합니다.");
    }

    @Test
    void updateGrade_Success() {
        // Given
        long gradeId = 1L;
        MemberGradeRequestDto updateDto = new MemberGradeRequestDto("Gold", 6000, 1500);
        when(memberGradeRepository.existsByGradeNameAndActive("Gold", true)).thenReturn(true);

        // When
        memberGradeService.updateGrade(gradeId, updateDto);

        // Then
        verify(memberGradeRepository, times(1)).updateMemberGrade(gradeId, updateDto);
    }

    @Test
    void updateGrade_NotFound() {
        // Given
        long gradeId = 1L;
        MemberGradeRequestDto updateDto = new MemberGradeRequestDto("Gold", 6000, 1500);
        when(memberGradeRepository.existsByGradeNameAndActive("Gold", true)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> memberGradeService.updateGrade(gradeId, updateDto))
            .isInstanceOf(MemberGradeNotFoundException.class)
            .hasMessage("Gold이 존재하지 않습니다");
    }

    @Test
    void getGradeDtoByName_Success() {
        // Given
        String gradeName = "Gold";
        when(memberGradeRepository.existsByGradeNameAndActive(gradeName, true)).thenReturn(true);
        when(memberGradeRepository.findByGradeName(gradeName)).thenReturn(testGrade);

        // When
        MemberGradeResponseDto responseDto = memberGradeService.getGradeDtoByName(gradeName);

        // Then
        assertThat(responseDto.gradeName()).isEqualTo("Gold");
    }

    @Test
    void getGradeDtoByName_NotFound() {
        // Given
        String nonExistentName = "NonExistent";
        when(memberGradeRepository.existsByGradeNameAndActive(nonExistentName, true)).thenReturn(
            false);

        // When & Then
        assertThatThrownBy(() -> memberGradeService.getGradeDtoByName(nonExistentName))
            .isInstanceOf(MemberGradeNotFoundException.class)
            .hasMessage(nonExistentName + "이 존재하지 않습니다.");
    }

    @Test
    void getGradeById_Success() {
        // Given
        long gradeId = 1L;
        when(memberGradeRepository.findById(gradeId)).thenReturn(Optional.of(testGrade));

        // When
        MemberGradeResponseDto responseDto = memberGradeService.getGradeById(gradeId);

        // Then
        assertThat(responseDto.gradeName()).isEqualTo("Gold");
    }

    @Test
    void getGradeById_NotFound() {
        // Given
        long nonExistentId = 999L;
        when(memberGradeRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> memberGradeService.getGradeById(nonExistentId))
            .isInstanceOf(MemberGradeNotFoundException.class)
            .hasMessage("등급이 존재하지 않습니다");
    }

    @Test
    void deleteGrade_Success() {
        // Given
        long gradeId = 1L;
        when(memberGradeRepository.existsById(gradeId)).thenReturn(true);

        // When
        memberGradeService.deleteGrade(gradeId);

        // Then
        verify(memberGradeRepository, times(1)).inactiveMemberGrade(gradeId);
    }

    @Test
    void deleteGrade_NotFound() {
        // Given
        long nonExistentId = 999L;
        when(memberGradeRepository.existsById(nonExistentId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> memberGradeService.deleteGrade(nonExistentId))
            .isInstanceOf(MemberGradeNotFoundException.class)
            .hasMessage("등급이 존재하지 않습니다.");
    }

    @Test
    void getGradeByName_Success() {
        // Given
        String gradeName = "Gold";
        when(memberGradeRepository.existsByGradeNameAndActive(gradeName, true)).thenReturn(true);
        when(memberGradeRepository.findByGradeName(gradeName)).thenReturn(testGrade);

        // When
        MemberGrade result = memberGradeService.getGradeByName(gradeName);

        // Then
        assertThat(result.getGradeName()).isEqualTo("Gold");
        assertThat(result.isActive()).isTrue();
        verify(memberGradeRepository, times(1)).findByGradeName(gradeName);
    }

    @Test
    void getGradeByName_NotFound() {
        // Given
        String nonExistentName = "NonExistent";
        when(memberGradeRepository.existsByGradeNameAndActive(nonExistentName, true)).thenReturn(
            false);

        // When & Then
        assertThatThrownBy(() -> memberGradeService.getGradeByName(nonExistentName))
            .isInstanceOf(MemberGradeNotFoundException.class)
            .hasMessage(nonExistentName + "이 존재하지 않습니다.");
    }

    @Test
    void getActiveGrades_Success() {
        // Given
        List<MemberGrade> activeGrades = List.of(
            new MemberGrade("Gold", 5000, 1000, true),
            new MemberGrade("Silver", 3000, 500, true)
        );
        when(memberGradeRepository.findByActive(true)).thenReturn(activeGrades);

        // When
        List<MemberGradeResponseDto> result = memberGradeService.getActiveGrades();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).gradeName()).isEqualTo("Gold");
        assertThat(result.get(1).gradeName()).isEqualTo("Silver");
        verify(memberGradeRepository, times(1)).findByActive(true);
    }

    @Test
    void getActiveGrades_NoActiveGrades() {
        // Given
        when(memberGradeRepository.findByActive(true)).thenReturn(List.of());

        // When
        List<MemberGradeResponseDto> result = memberGradeService.getActiveGrades();

        // Then
        assertThat(result).isEmpty();
        verify(memberGradeRepository, times(1)).findByActive(true);
    }

}
