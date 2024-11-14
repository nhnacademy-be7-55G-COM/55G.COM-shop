package shop.s5g.shop.entity.member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Member member;

    private String primaryAddress;

    private String detailAddress;

    @Setter
    private String alias;

    @Column(name = "is_default")
    @Setter
    private boolean isDefault;

    public Address(Member member, String primaryAddress, String detailAddress, String alias,
        boolean isDefault) {
        this.member = member;
        this.primaryAddress = primaryAddress;
        this.detailAddress = detailAddress;
        this.alias = alias;
        this.isDefault = isDefault;
    }
}
