package shop.S5G.shop.entity.member;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MemberGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberGradeId;

    private String gradeName;

    private int gradeCondition;

    private int point;

    private boolean active;
}
