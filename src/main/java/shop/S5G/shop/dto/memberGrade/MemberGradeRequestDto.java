package shop.S5G.shop.dto.memberGrade;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberGradeRequestDto {

    @NotNull
    @Length(max = 15)
    private String gradeName;

    @NotNull
    private int gradeCondition;

    @NotNull
    private int point;
}
