package shop.s5g.shop.repository.coupon.coupon.qdsl;

import static shop.s5g.shop.entity.coupon.QCoupon.coupon;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.coupon.coupon.CouponResponseDto;
import shop.s5g.shop.entity.coupon.Coupon;
import shop.s5g.shop.entity.coupon.QCoupon;

public class CouponQuerydslRepositoryImpl extends QuerydslRepositorySupport implements CouponQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private static final boolean ACTIVE = true;
    private static final boolean INACTIVE = false;

    public CouponQuerydslRepositoryImpl(EntityManager em) {
        super(Coupon.class);
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    /**
     * 쿠폰 만료날짜 업데이트 쿼리dsl
     * @param couponId
     * @param expiredAt
     */
    @Override
    public void updateCouponExpiredDatetime(Long couponId, LocalDateTime expiredAt) {
        update(coupon)
            .set(coupon.expiredAt, expiredAt)
            .where(coupon.couponId.eq(couponId))
            .execute();
    }

    /**
     * 특정 쿠폰 조회 쿼리dsl
     * @param couponId
     * @return CouponResponseDto
     */
    @Override
    public CouponResponseDto findCoupon(Long couponId) {

        QCoupon qCoupon = QCoupon.coupon;

        return jpaQueryFactory.select(Projections.constructor(
            CouponResponseDto.class,
                qCoupon.couponTemplate,
                qCoupon.couponCode,
                qCoupon.createdAt,
                qCoupon.expiredAt,
                qCoupon.usedAt))
            .from(qCoupon)
            .where(qCoupon.couponId.eq(couponId))
            .fetchOne();
    }

    /**
     * 쿠폰 삭제 쿼리dsl
     * @param couponId
     */
    @Override
    public void deleteCouponById(Long couponId) {
        update(coupon)
            .set(coupon.active, INACTIVE)
            .where(coupon.couponId.eq(couponId)
                .and(coupon.active.eq(ACTIVE)))
            .execute();
    }

    /**
     * 쿠폰 삭제 여부 확인 쿼리dsl
     * @param couponId
     * @return TRUE or FALSE
     */
    @Override
    public boolean checkActiveCoupon(Long couponId) {

        QCoupon coupon = QCoupon.coupon;

        return Boolean.TRUE.equals(jpaQueryFactory
            .select(coupon.active)
            .from(coupon)
            .where(coupon.couponId.eq(couponId))
            .fetchOne());
    }
}
