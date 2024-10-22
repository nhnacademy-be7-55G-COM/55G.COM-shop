package shop.S5G.shop.entity.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "member_status_id")
    private MemberStatus status;

    @ManyToOne
    @JoinColumn(name = "member_grade_id")
    private MemberGrade grade;

    private String loginId;
    private String password;
    private String birth;
    private LocalDateTime createdAt;
    private LocalDateTime lastestLoginAt;
    private Long point;

    public Member(MemberStatus status, MemberGrade grade, String loginId, String password, String birth, LocalDateTime createdAt, LocalDateTime lastestLoginAt, Long point) {
        this.status = status;
        this.grade = grade;
        this.loginId = loginId;
        this.password = password;
        this.birth = birth;
        this.createdAt = createdAt;
        this.lastestLoginAt = lastestLoginAt;
        this.point = point;
    }
}
