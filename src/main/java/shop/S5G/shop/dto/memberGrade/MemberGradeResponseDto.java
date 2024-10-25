package shop.S5G.shop.dto.memberGrade;

public record MemberGradeResponseDto(
    Long memberGradeId,

    String gradeName,

    int gradeCondition,

    int point
){
}
