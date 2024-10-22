package shop.S5G.shop.dto.memberGrade;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberGradeResponseDto {
    private Long memberGradeId;

    private String gradeName;

    private int gradeCondition;

    private int point;
}
