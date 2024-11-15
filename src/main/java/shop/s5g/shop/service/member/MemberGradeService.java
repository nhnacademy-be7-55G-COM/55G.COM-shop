package shop.s5g.shop.service.member;

import java.util.List;
import shop.s5g.shop.dto.memberGrade.MemberGradeRequestDto;
import shop.s5g.shop.dto.memberGrade.MemberGradeResponseDto;
import shop.s5g.shop.entity.member.MemberGrade;

public interface MemberGradeService {

    void addGrade(MemberGradeRequestDto grade);

    void updateGrade(long gradeId, MemberGradeRequestDto grade);

    MemberGradeResponseDto getGradeDtoByName(String name);

    MemberGrade getGradeByName(String name);

    MemberGradeResponseDto getGradeById(long id);

    List<MemberGradeResponseDto> getActiveGrades();

    void deleteGrade(long gradeId);

}
