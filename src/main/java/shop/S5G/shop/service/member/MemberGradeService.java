package shop.S5G.shop.service.member;

import shop.S5G.shop.dto.memberGrade.MemberGradeRequestDto;
import shop.S5G.shop.dto.memberGrade.MemberGradeResponseDto;
import shop.S5G.shop.entity.member.MemberGrade;

import java.util.List;

public interface MemberGradeService {
    void addGrade(MemberGradeRequestDto grade);
    void updateGrade(long gradeId, MemberGradeRequestDto grade);
    MemberGradeResponseDto getGradeByName(String name);
    MemberGradeResponseDto getGradeById(long id);
    List<MemberGradeResponseDto> getActiveGrades();
    void deleteGrade(long gradeId);

    // 이름으로 존재 유무 판단
    boolean existsGradeByName(String name);

    // 기본키로 존재 유무 판단
    boolean existsGradeById(long gradeId);
}
