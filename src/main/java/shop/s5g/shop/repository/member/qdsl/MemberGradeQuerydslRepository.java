package shop.s5g.shop.repository.member.qdsl;

import shop.s5g.shop.dto.member_grade.MemberGradeRequestDto;

public interface MemberGradeQuerydslRepository {

    void updateMemberGrade(long gradeId, MemberGradeRequestDto memberGradeRequestDto);

    void inactiveMemberGrade(long gradeId);
}
