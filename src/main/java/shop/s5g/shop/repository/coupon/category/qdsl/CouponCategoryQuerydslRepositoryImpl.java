package shop.s5g.shop.repository.coupon.category.qdsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.entity.coupon.CouponCategory;

public class CouponCategoryQuerydslRepositoryImpl extends QuerydslRepositorySupport implements CouponCategoryQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CouponCategoryQuerydslRepositoryImpl(EntityManager em) {
        super(CouponCategory.class);
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }


}
