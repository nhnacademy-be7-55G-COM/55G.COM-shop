package shop.s5g.shop.dto.member_grade;

import shop.s5g.shop.entity.member.MemberGrade;

public record MemberGradeResponseDto(
    Long memberGradeId,

    String gradeName,

    int gradeCondition,

    int point
) {

    public static MemberGradeResponseDto toDto(MemberGrade memberGrade) {
        return new MemberGradeResponseDto(memberGrade.getMemberGradeId(),
            memberGrade.getGradeName(), memberGrade.getGradeCondition(), memberGrade.getPoint());
    }
}
