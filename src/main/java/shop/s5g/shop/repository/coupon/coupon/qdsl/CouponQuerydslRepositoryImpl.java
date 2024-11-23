package shop.s5g.shop.repository.coupon.coupon.qdsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

    QCoupon coupon = QCoupon.coupon;

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

        return jpaQueryFactory.select(Projections.constructor(
            CouponResponseDto.class,
                coupon.couponId,
                coupon.couponTemplate.couponTemplateId,
                coupon.couponCode,
                coupon.createdAt,
                coupon.expiredAt,
                coupon.usedAt))
            .from(coupon)
            .where(coupon.couponId.eq(couponId))
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

        return Boolean.TRUE.equals(jpaQueryFactory
            .select(coupon.active)
            .from(coupon)
            .where(coupon.couponId.eq(couponId))
            .fetchOne());
    }

    /**
     * 관리자가 발급한 모든 쿠폰 항목 보기
     * @param pageable
     * @return
     */
    @Override
    public Page<CouponResponseDto> getAllIssuedCoupons(Pageable pageable) {

        List<CouponResponseDto> couponList = jpaQueryFactory
            .select(Projections.constructor(CouponResponseDto.class,
                coupon.couponId,
                coupon.couponTemplate.couponTemplateId,
                coupon.couponCode,
                coupon.createdAt,
                coupon.expiredAt,
                coupon.usedAt))
            .from(coupon)
            .where(coupon.active.eq(ACTIVE))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long totalCnt = jpaQueryFactory
            .select(coupon.count())
            .from(coupon)
            .fetchOne();

        long total = (Objects.nonNull(totalCnt)) ? totalCnt : 0L;

        return new PageImpl<>(couponList, pageable, total);
    }

    /**
     * 만료일이 지난 쿠폰 비활성화 해주기
     */
    @Override
    public void deactivateExpiredCoupons() {

        jpaQueryFactory
            .update(coupon)
            .set(coupon.active, INACTIVE)
            .where(coupon.expiredAt.before(LocalDateTime.now()))
            .execute();
    }

    /**
     * 해당 쿠폰이 만료일이 지났는 지 체크해주기
     * @param couponId
     * @return boolean
     */
    @Override
    public boolean checkIfCouponExpired(Long couponId) {
        return Boolean.TRUE.equals(jpaQueryFactory
            .select(coupon.expiredAt.before(LocalDateTime.now()))
            .from(coupon)
            .where(coupon.couponId.eq(couponId))
            .fetchOne());
    }
}
