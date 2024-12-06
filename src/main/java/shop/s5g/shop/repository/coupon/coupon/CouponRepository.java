package shop.s5g.shop.repository.coupon.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.coupon.Coupon;
import shop.s5g.shop.repository.coupon.coupon.qdsl.CouponQuerydslRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponQuerydslRepository {

    boolean existsCouponByCouponCode(String couponCode);

    Coupon findByCouponCode(String couponCode);
}
