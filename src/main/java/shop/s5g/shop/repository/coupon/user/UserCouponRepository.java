package shop.s5g.shop.repository.coupon.user;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.coupon.UserCoupon;
import shop.s5g.shop.repository.coupon.user.qdsl.UserCouponQuerydslRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long>,
    UserCouponQuerydslRepository {

}
