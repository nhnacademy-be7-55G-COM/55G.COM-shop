package shop.S5G.shop.entity.member;

import jakarta.persistence.*;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Member member;

    private String primaryAddress;

    private String detailAddress;

    private String alias;

    private boolean isDefault;
}
