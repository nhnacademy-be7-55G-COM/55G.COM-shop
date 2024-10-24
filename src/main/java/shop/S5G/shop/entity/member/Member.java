package shop.S5G.shop.entity.member;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private LocalDateTime latestLoginAt;
    private Long point;

    public Member(Customer customer, MemberStatus status, MemberGrade grade, String loginId,
        String password, String birth, LocalDateTime createdAt, LocalDateTime latestLoginAt,
        Long point) {
        this.customer = customer;
        this.status = status;
        this.grade = grade;
        this.loginId = loginId;
        this.password = password;
        this.birth = birth;
        this.createdAt = createdAt;
        this.latestLoginAt = latestLoginAt;
        this.point = point;
    }
}
