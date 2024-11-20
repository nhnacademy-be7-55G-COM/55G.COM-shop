package shop.s5g.shop.repository.coupon.user.qdsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.coupon.user.UserCouponRequestDto;
import shop.s5g.shop.entity.coupon.UserCoupon;

public class UserCouponQuerydslRepositoryImpl extends QuerydslRepositorySupport implements
    UserCouponQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public UserCouponQuerydslRepositoryImpl(JPAQueryFactory queryFactory) {
        super(UserCoupon.class);
        this.queryFactory = queryFactory;
    }

}
