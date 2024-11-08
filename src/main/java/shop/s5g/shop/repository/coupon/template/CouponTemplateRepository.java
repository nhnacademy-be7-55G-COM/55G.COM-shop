package shop.s5g.shop.repository.coupon.template;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.repository.coupon.template.qdsl.CouponTemplateQuerydslRepository;

public interface CouponTemplateRepository extends JpaRepository<CouponTemplate, Long>,
    CouponTemplateQuerydslRepository {

}
