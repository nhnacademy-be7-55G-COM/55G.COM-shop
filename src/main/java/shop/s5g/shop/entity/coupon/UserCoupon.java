package shop.s5g.shop.entity.coupon;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.s5g.shop.entity.member.Member;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "user_coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon {

    @EmbeddedId
    private UserCouponPk userCouponPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(value = "customerId")
    @JoinColumn(name = "customer_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId(value = "couponId")
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    public UserCoupon(Member member, Coupon coupon) {
        this.member = member;
        this.coupon = coupon;
        this.userCouponPk = new UserCouponPk(member.getId(), coupon.getCouponId());
    }
}
