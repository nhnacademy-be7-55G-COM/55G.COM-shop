package shop.S5G.shop.entity.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "coupon_policy")
public class CouponPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_policy_id")
    private Long couponPolicyId;

    @Min(0)
    @NotNull
    @Column(name = "discount_price")
    private BigDecimal discountPrice;

    @NotNull
    @Column(name = "condition")
    private Long condition;

    @Column(name = "max_price")
    private Long maxPrice;

    @NotNull
    @Column(name = "duration")
    private Integer duration;

    public CouponPolicy(BigDecimal discountPrice, Long condition, Long maxPrice, Integer duration) {
        this.discountPrice = discountPrice;
        this.condition = condition;
        this.maxPrice = maxPrice;
        this.duration = duration;
    }
}
