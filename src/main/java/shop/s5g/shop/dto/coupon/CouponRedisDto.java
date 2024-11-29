package shop.s5g.shop.dto.coupon;

import lombok.Data;
import shop.s5g.shop.entity.coupon.Coupon;
import shop.s5g.shop.entity.coupon.CouponTemplate;

@Data
public class CouponRedisDto {
    private Long couponId;
    private Long couponTemplateId;
    private String couponCode;
    private boolean active;

    public CouponRedisDto(Coupon coupon) {
        this.couponId = coupon.getCouponId();
        this.couponTemplateId = coupon.getCouponTemplate().getCouponTemplateId();
        this.couponCode = coupon.getCouponCode();
        this.active = coupon.isActive();
    }

    public Coupon toEntity(CouponTemplate couponTemplate) {
        return new Coupon(couponTemplate, couponCode);
    }
}
