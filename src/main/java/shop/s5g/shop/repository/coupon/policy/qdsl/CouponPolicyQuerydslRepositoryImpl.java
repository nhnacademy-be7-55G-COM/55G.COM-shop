package shop.s5g.shop.repository.coupon.policy.qdsl;

import static shop.s5g.shop.entity.coupon.QCouponPolicy.couponPolicy;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.coupon.policy.CouponPolicyResponseDto;
import shop.s5g.shop.entity.coupon.CouponPolicy;

public class CouponPolicyQuerydslRepositoryImpl extends QuerydslRepositorySupport implements CouponPolicyQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public CouponPolicyQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(CouponPolicy.class);
        this.queryFactory = jpaQueryFactory;
    }

    /**
     * 쿠폰 정책 업데이트 쿼리 Dsl
     * @param couponId
     * @param discountPrice
     * @param condition
     * @param maxPrice
     * @param duration
     */
    @Override
    public void updateCouponPolicy(Long couponId, BigDecimal discountPrice, Long condition,
        Long maxPrice, Integer duration) {
        update(couponPolicy)
            .set(couponPolicy.discountPrice, discountPrice)
            .set(couponPolicy.condition, condition)
            .set(couponPolicy.maxPrice, maxPrice)
            .set(couponPolicy.duration, duration)
            .where(couponPolicy.couponPolicyId.eq(couponId))
            .execute();
    }

    @Override
    public Page<CouponPolicyResponseDto> findAllCouponPolicies(Pageable pageable) {

        List<CouponPolicyResponseDto> policyList = queryFactory
            .select(Projections.constructor(CouponPolicyResponseDto.class,
                couponPolicy.couponPolicyId,
                couponPolicy.discountPrice,
                couponPolicy.condition,
                couponPolicy.maxPrice,
                couponPolicy.duration))
            .from(couponPolicy)
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        Long totalCnt = queryFactory
            .select(couponPolicy.count())
            .from(couponPolicy)
            .fetchOne();

        long total = (Objects.isNull(totalCnt)) ? 0L : totalCnt;

        return new PageImpl<>(policyList, pageable, total);
    }

}
