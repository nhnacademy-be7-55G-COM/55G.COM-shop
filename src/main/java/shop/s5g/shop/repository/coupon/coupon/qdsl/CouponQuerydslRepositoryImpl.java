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
import shop.s5g.shop.dto.coupon.coupon.AvailableCouponResponseDto;
import shop.s5g.shop.dto.coupon.coupon.CouponResponseDto;
import shop.s5g.shop.entity.coupon.Coupon;
import shop.s5g.shop.entity.coupon.CouponTemplate.CouponTemplateType;
import shop.s5g.shop.entity.coupon.QCoupon;
import shop.s5g.shop.entity.coupon.QCouponPolicy;
import shop.s5g.shop.entity.coupon.QCouponTemplate;

public class CouponQuerydslRepositoryImpl extends QuerydslRepositorySupport implements CouponQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private static final boolean ACTIVE = true;
    private static final boolean INACTIVE = false;

    public CouponQuerydslRepositoryImpl(EntityManager em) {
        super(Coupon.class);
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    QCoupon coupon = QCoupon.coupon;
    QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;
    QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;


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
                coupon.couponCode
            ))
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
     * @return Page<CouponResponseDto>
     */
    @Override
    public Page<CouponResponseDto> getAllIssuedCoupons(Pageable pageable) {

        List<CouponResponseDto> couponList = jpaQueryFactory
            .select(Projections.constructor(CouponResponseDto.class,
                coupon.couponId,
                coupon.couponTemplate.couponTemplateId,
                coupon.couponCode
            ))
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
     * 발급 가능한 쿠폰 목록 조회
     * @param pageable
     * @return Page<AvailableCouponResponseDto>
     */
    @Override
    public Page<AvailableCouponResponseDto> getAllAvailableCoupons(Pageable pageable) {

        List<AvailableCouponResponseDto> availableCouponList = jpaQueryFactory
            .select(Projections.constructor(AvailableCouponResponseDto.class,
                coupon.couponId.min(),
                coupon.couponTemplate.couponTemplateId,
                couponTemplate.couponName,
                couponPolicy.condition,
                couponPolicy.maxPrice,
                couponPolicy.discountPrice,
                coupon.couponId.count()
            ))
            .from(coupon)
            .innerJoin(couponTemplate).on(coupon.couponTemplate.couponTemplateId.eq(couponTemplate.couponTemplateId))
            .innerJoin(couponPolicy).on(couponTemplate.couponPolicy.couponPolicyId.eq(couponPolicy.couponPolicyId))
            .where(
                couponTemplate.couponName.contains(CouponTemplateType.BIRTH.getTypeName()).not()
                    .and(couponTemplate.couponName.contains(CouponTemplateType.WELCOME.getTypeName())).not()
            )
            .groupBy(
                coupon.couponTemplate.couponTemplateId,
                couponTemplate.couponName,
                couponPolicy.condition,
                couponPolicy.maxPrice,
                couponPolicy.discountPrice
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = jpaQueryFactory
            .select(coupon.couponTemplate.couponTemplateId.countDistinct())
            .from(coupon)
            .fetchOne();

        long totalCount = (total != null) ? total : 0L;

        return new PageImpl<>(availableCouponList, pageable, totalCount);
    }
}
