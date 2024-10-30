package shop.S5G.shop.repository.coupon.policy.qdsl;

import static shop.S5G.shop.entity.coupon.QCouponPolicy.couponPolicy;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.S5G.shop.dto.coupon.policy.CouponPolicyResponseDto;
import shop.S5G.shop.entity.coupon.CouponPolicy;

public class CouponPolicyQuerydslRepositoryImpl extends QuerydslRepositorySupport implements CouponPolicyQuerydslRepository {

    public CouponPolicyQuerydslRepositoryImpl() {
        super(CouponPolicy.class);
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

    //TODO(Doo) : 나중에 작성 - Querydsl 이용해 List Dto 반환
    @Override
    public List<CouponPolicyResponseDto> findAllCouponPolicies() {
        return List.of();
    }

}
