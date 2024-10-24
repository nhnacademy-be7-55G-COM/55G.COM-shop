package shop.S5G.shop.entity.member;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "member_status")
public class MemberStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberStatusId;

    private String typeName;

    public MemberStatus(String typeName) {
        this.typeName = typeName;
    }

}
