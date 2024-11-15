package shop.s5g.shop.repository.coupon.policy;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.repository.coupon.policy.qdsl.CouponPolicyQuerydslRepository;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long>, CouponPolicyQuerydslRepository {


}
