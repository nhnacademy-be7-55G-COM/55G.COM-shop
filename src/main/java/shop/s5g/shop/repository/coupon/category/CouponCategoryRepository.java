package shop.s5g.shop.repository.coupon.category;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.coupon.CouponCategory;
import shop.s5g.shop.repository.coupon.category.qdsl.CouponCategoryQuerydslRepository;

public interface CouponCategoryRepository extends JpaRepository<CouponCategory, Long>,
    CouponCategoryQuerydslRepository {

}
