package shop.S5G.shop.repository.coupon.coupon.qdsl;

import static shop.S5G.shop.entity.coupon.QCoupon.coupon;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.S5G.shop.dto.coupon.coupon.CouponResponseDto;
import shop.S5G.shop.entity.coupon.Coupon;
import shop.S5G.shop.entity.coupon.QCoupon;

public class CouponQuerydslRepositoryImpl extends QuerydslRepositorySupport implements CouponQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private static final boolean ACTIVE = true;
    private static final boolean INACTIVE = false;

    public CouponQuerydslRepositoryImpl(EntityManager em) {
        super(Coupon.class);
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public void updateCouponExpiredDatetime(Long couponId, LocalDateTime expiredAt) {
        update(coupon)
            .set(coupon.expiredAt, expiredAt)
            .where(coupon.couponId.eq(couponId))
            .execute();
    }

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

    @Override
    public List<CouponResponseDto> findCoupons(Pageable pageable) {

        QCoupon coupon = QCoupon.coupon;

        return jpaQueryFactory
            .select(Projections.constructor(CouponResponseDto.class,
                coupon.couponTemplate,
                coupon.couponCode,
                coupon.createdAt,
                coupon.expiredAt,
                coupon.usedAt))
            .from(coupon)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }

    @Override
    public void deleteCouponById(Long couponId) {
        update(coupon)
            .set(coupon.active, INACTIVE)
            .where(coupon.couponId.eq(couponId)
                .and(coupon.active.eq(ACTIVE)))
            .execute();
    }
}
