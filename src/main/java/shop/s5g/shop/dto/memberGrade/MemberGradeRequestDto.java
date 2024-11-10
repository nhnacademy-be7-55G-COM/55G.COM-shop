package shop.s5g.shop.dto.memberGrade;


import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record MemberGradeRequestDto(

    @NotNull @Length(min = 1, max = 15) String gradeName,

    @NotNull int gradeCondition,

    @NotNull int point) {

}
