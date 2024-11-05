package shop.S5G.shop.repository.coupon.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.coupon.Coupon;
import shop.S5G.shop.repository.coupon.coupon.qdsl.CouponQuerydslRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long>, CouponQuerydslRepository {


}
