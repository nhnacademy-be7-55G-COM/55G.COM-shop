package shop.s5g.shop.entity.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "member_grade")
public class MemberGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberGradeId;

    private String gradeName;

    private int gradeCondition;

    private int point;

    private boolean active;

    public MemberGrade(String gradeName, int gradeCondition, int point, boolean active) {
        this.gradeName = gradeName;
        this.gradeCondition = gradeCondition;
        this.point = point;
        this.active = active;
    }

}
