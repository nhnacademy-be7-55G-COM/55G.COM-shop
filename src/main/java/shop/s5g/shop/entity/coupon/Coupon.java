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

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    private LocalDateTime usedAt;

    @NotNull
    private boolean active;

    public Coupon(CouponTemplate couponTemplate, String couponCode, LocalDateTime expiredAt) {
        this.couponTemplate = couponTemplate;
        this.couponCode = couponCode;
        this.createdAt = LocalDateTime.now();
        this.expiredAt = expiredAt;
        this.usedAt = null;
        this.active = true;
    }

    public enum CouponType {
        WELCOME("Welcome"), BIRTH("Birth"), BOOK("도서"), CATEGORY("카테고리");

        private final String dataName;

        CouponType(String name) { this.dataName = name; }
    }
}
