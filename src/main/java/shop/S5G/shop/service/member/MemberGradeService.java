package shop.S5G.shop.service.member;

import java.util.List;
import shop.S5G.shop.dto.memberGrade.MemberGradeRequestDto;
import shop.S5G.shop.dto.memberGrade.MemberGradeResponseDto;
import shop.S5G.shop.entity.member.MemberGrade;

public interface MemberGradeService {

    void addGrade(MemberGradeRequestDto grade);

    void updateGrade(long gradeId, MemberGradeRequestDto grade);

    MemberGradeResponseDto getGradeDtoByName(String name);

    MemberGrade getGradeByName(String name);

    MemberGradeResponseDto getGradeById(long id);

    List<MemberGradeResponseDto> getActiveGrades();

    void deleteGrade(long gradeId);

}
