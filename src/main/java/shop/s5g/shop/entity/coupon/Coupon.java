package shop.s5g.shop.entity.coupon;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Entity
@Table(name = "coupon")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_template_id")
    private CouponTemplate couponTemplate;

    @NotBlank
    @Length(min = 8, max = 50)
    private String couponCode;

    @NotNull
    private boolean active;

    public Coupon(CouponTemplate couponTemplate, String couponCode) {
        this.couponTemplate = couponTemplate;
        this.couponCode = couponCode;
        this.active = true;
    }
}
