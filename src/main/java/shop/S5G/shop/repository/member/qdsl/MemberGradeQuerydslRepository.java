package shop.S5G.shop.repository.member.qdsl;

import shop.S5G.shop.dto.memberGrade.MemberGradeRequestDto;

public interface MemberGradeQuerydslRepository {

    void updateMemberGrade(long gradeId, MemberGradeRequestDto memberGradeRequestDto);

    void inactiveMemberGrade(long gradeId);
}
