package shop.S5G.shop.dto.memberGrade;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberGradeRequestDto {

    private String gradeName;

    private int gradeCondition;

    private int point;

}
