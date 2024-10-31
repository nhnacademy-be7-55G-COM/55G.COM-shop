package shop.S5G.shop.repository.coupon.policy;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.coupon.CouponPolicy;
import shop.S5G.shop.repository.coupon.policy.qdsl.CouponPolicyQuerydslRepository;

public interface CouponPolicyRepository extends JpaRepository<CouponPolicy, Long>, CouponPolicyQuerydslRepository {


}
