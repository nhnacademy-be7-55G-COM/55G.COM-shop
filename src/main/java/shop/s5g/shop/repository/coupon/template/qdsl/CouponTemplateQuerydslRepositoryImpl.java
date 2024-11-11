package shop.s5g.shop.repository.coupon.template.qdsl;

import static shop.s5g.shop.entity.coupon.QCouponTemplate.couponTemplate;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.entity.coupon.QCoupon;
import shop.s5g.shop.entity.coupon.QCouponTemplate;

public class CouponTemplateQuerydslRepositoryImpl extends QuerydslRepositorySupport implements CouponTemplateQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private static final boolean ACTIVE = true;
    private static final boolean INACTIVE = false;

    public CouponTemplateQuerydslRepositoryImpl(EntityManager em) {
        super(CouponTemplate.class);
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public CouponTemplateResponseDto findCouponTemplateById(Long couponTemplateId) {

        QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;

        return jpaQueryFactory
            .select(Projections.constructor(
                CouponTemplateResponseDto.class,
                couponTemplate.couponPolicy,
                couponTemplate.couponName,
                couponTemplate.couponDescription
            ))
            .from(couponTemplate)
            .where(couponTemplate.couponTemplateId.eq(couponTemplateId))
            .fetchOne();
    }

    @Override
    public void updateCouponTemplate(Long couponTemplateId, CouponPolicy couponPolicy, CouponTemplateRequestDto couponTemplateRequestDto) {
        update(couponTemplate)
            .set(couponTemplate.couponPolicy, couponPolicy)
            .set(couponTemplate.couponName, couponTemplateRequestDto.couponName())
            .set(couponTemplate.couponDescription, couponTemplateRequestDto.couponDescription())
            .set(couponTemplate.active, ACTIVE)
            .where(couponTemplate.couponTemplateId.eq(couponTemplateId))
            .execute();
    }

    @Override
    public boolean checkActiveCouponTemplate(Long couponTemplateId) {

        QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;

        return Boolean.TRUE.equals(jpaQueryFactory
            .select(couponTemplate.active)
            .from(couponTemplate)
            .where(couponTemplate.couponTemplateId.eq(couponTemplateId))
            .fetchOne());
    }

    @Override
    public void deleteCouponTemplate(Long couponTemplateId) {

        QCoupon coupon = QCoupon.coupon;

        update(couponTemplate)
            .set(couponTemplate.active, INACTIVE)
            .where(couponTemplate.couponTemplateId.eq(couponTemplateId))
            .execute();

        update(coupon)
            .set(coupon.active, INACTIVE)
            .where(coupon.couponTemplate.couponTemplateId.eq(couponTemplateId))
            .execute();
    }
}