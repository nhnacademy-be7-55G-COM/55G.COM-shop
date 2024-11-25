package shop.s5g.shop.entity.coupon;

import jakarta.persistence.Column;
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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "coupon_template")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_template_id")
    private Long couponTemplateId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_policy_id")
    private CouponPolicy couponPolicy;

    @Setter
    @NotBlank
    @Length(min = 2, max = 50)
    private String couponName;

    @Setter
    @NotBlank
    @Length(min = 2, max = 200)
    private String couponDescription;

    @Setter
    @NotNull
    private boolean active;

    public CouponTemplate(CouponPolicy couponPolicy, String couponName, String couponDescription) {
        this.couponPolicy = couponPolicy;
        this.couponName = couponName;
        this.couponDescription = couponDescription;
        this.active = true;
    }

    @Getter
    public enum CouponTemplateType {
        WELCOME("Welcome"),
        BIRTH("Birth"),
        BOOK("Book"),
        CATEGORY("Category"),
        FREE("Free");

        private final String typeName;

        private CouponTemplateType(String typeName) {
            this.typeName = typeName;
        }
    }
}
