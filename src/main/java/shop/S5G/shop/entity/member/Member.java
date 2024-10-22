package shop.S5G.shop.entity.member;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
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

}
