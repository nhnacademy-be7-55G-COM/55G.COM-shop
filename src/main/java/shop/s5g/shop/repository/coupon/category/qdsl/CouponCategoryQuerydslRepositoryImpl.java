package shop.s5g.shop.repository.coupon.category.qdsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.entity.coupon.CouponCategory;
import shop.s5g.shop.entity.coupon.QCouponCategory;

public class CouponCategoryQuerydslRepositoryImpl extends QuerydslRepositorySupport implements CouponCategoryQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CouponCategoryQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(CouponCategory.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    QCouponCategory couponCategory = QCouponCategory.couponCategory;

    /**
     * 카테고리 쿠폰이 존재여부 체크
     * @param couponTemplateId
     * @param categoryId
     * @return boolean
     */
    @Override
    public boolean existsByCouponTemplateAndCategory(Long couponTemplateId, Long categoryId) {
        return !jpaQueryFactory
            .selectFrom(couponCategory)
            .where(couponCategory.category.categoryId.eq(categoryId)
                .and(couponCategory.couponTemplate.couponTemplateId.eq(couponTemplateId)))
            .fetch()
            .isEmpty();
    }
}
