package shop.S5G.shop.service.member;

import shop.S5G.shop.dto.memberGrade.MemberGradeRequestDto;
import shop.S5G.shop.entity.member.MemberGrade;

import java.util.List;

public interface MemberGradeService {
    void addGrade(MemberGradeRequestDto grade);
    void updateGrade(long gradeId, MemberGradeRequestDto grade);
    MemberGrade getGradeByName(String name);
    List<MemberGrade> getAllGrades();
    void deleteGrade(long gradeId);

    // 이름으로 존재 유무 판단
    boolean existsGradeByName(String name);

    // 기본키로 존재 유무 판단
    boolean existsGradeById(long gradeId);
}
