package shop.S5G.shop.dto.memberGrade;

import shop.S5G.shop.entity.member.MemberGrade;

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
