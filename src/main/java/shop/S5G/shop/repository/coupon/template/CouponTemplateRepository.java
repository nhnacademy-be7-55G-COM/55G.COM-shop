package shop.S5G.shop.repository.coupon.template;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.coupon.CouponTemplate;
import shop.S5G.shop.repository.coupon.template.qdsl.CouponTemplateQuerydslRepository;

public interface CouponTemplateRepository extends JpaRepository<CouponTemplate, Long>,
    CouponTemplateQuerydslRepository {

}
