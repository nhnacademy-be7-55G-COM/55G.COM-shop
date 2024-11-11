package shop.s5g.shop.repository.coupon.category.qdsl;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.entity.coupon.CouponCategory;

public class CouponCategoryQuerydslRepositoryImpl extends QuerydslRepositorySupport implements CouponCategoryQuerydslRepository {

    public CouponCategoryQuerydslRepositoryImpl() {
        super(CouponCategory.class);
    }
}
