package shop.s5g.shop.repository.coupon.template.qdsl;

import static shop.s5g.shop.entity.coupon.QCouponTemplate.couponTemplate;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.entity.coupon.QCoupon;
import shop.s5g.shop.entity.coupon.QCouponBook;
import shop.s5g.shop.entity.coupon.QCouponCategory;
import shop.s5g.shop.entity.coupon.QCouponPolicy;
import shop.s5g.shop.entity.coupon.QCouponTemplate;

public class CouponTemplateQuerydslRepositoryImpl extends QuerydslRepositorySupport implements CouponTemplateQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private static final boolean ACTIVE = true;
    private static final boolean INACTIVE = false;

    public CouponTemplateQuerydslRepositoryImpl(EntityManager em) {
        super(CouponTemplate.class);
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    /**
     * 특정 쿠폰 템플릿 찾기 쿼리 dsl
     * @param couponTemplateId
     * @return CouponTemplateResponseDto
     */
    @Override
    public CouponTemplateResponseDto findCouponTemplateById(Long couponTemplateId) {

        QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;

        return jpaQueryFactory
            .select(Projections.constructor(
                CouponTemplateResponseDto.class,
                couponTemplate.couponTemplateId,
                couponTemplate.couponPolicy.discountPrice,
                couponTemplate.couponPolicy.condition,
                couponTemplate.couponPolicy.maxPrice,
                couponTemplate.couponPolicy.duration,
                couponTemplate.couponName,
                couponTemplate.couponDescription
            ))
            .from(couponTemplate)
            .where(couponTemplate.couponTemplateId.eq(couponTemplateId))
            .fetchOne();
    }

    /**
     * 쿠폰 템플릿 업데이트 쿼리 dsl
     * @param couponTemplateId
     * @param couponPolicy
     * @param couponTemplateRequestDto
     */
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

    /**
     * 쿠폰 템플릿 삭제 여부 확인 쿼리 dsl
     * @param couponTemplateId
     * @return boolean
     */
    @Override
    public boolean checkActiveCouponTemplate(Long couponTemplateId) {

        QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;

        return Boolean.TRUE.equals(jpaQueryFactory
            .select(couponTemplate.active)
            .from(couponTemplate)
            .where(couponTemplate.couponTemplateId.eq(couponTemplateId))
            .fetchOne());
    }

    /**
     * 쿠폰 템플릿 삭제 쿼리 dsl
     * @param couponTemplateId
     */
    @Override
    public void deleteCouponTemplate(Long couponTemplateId) {

        QCoupon coupon = QCoupon.coupon;
        QCouponBook couponBook = QCouponBook.couponBook;
        QCouponCategory couponCategory = QCouponCategory.couponCategory;

        // 쿠폰 템플릿 상태 변환
        update(couponTemplate)
            .set(couponTemplate.active, INACTIVE)
            .where(couponTemplate.couponTemplateId.eq(couponTemplateId))
            .execute();

        // 주인 없는 쿠폰 상태 변환
        update(coupon)
            .set(coupon.active, INACTIVE)
            .where(coupon.couponTemplate.couponTemplateId.eq(couponTemplateId))
            .execute();

        // 관련된 책에 적용된 쿠폰 삭제
        delete(couponBook)
            .where(couponBook.couponTemplate.couponTemplateId.eq(couponTemplateId))
            .execute();

        // 관련된 카테고리에 적용된 쿠폰 삭제
        delete(couponCategory)
            .where(couponCategory.couponTemplate.couponTemplateId.eq(couponTemplateId))
            .execute();
    }

    /**
     * 쿠폰 템플릿 찾기 쿼리 dsl
     * @param pageable
     * @return List<CouponTemplateResponseDto>
     */
    @Override
    public List<CouponTemplateResponseDto> findCouponTemplatesByPageable(Pageable pageable) {

        QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;
        QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;

        return from(couponTemplate)
            .innerJoin(couponTemplate.couponPolicy, couponPolicy)
            .where(couponTemplate.active.eq(true))
            .select(Projections.constructor(CouponTemplateResponseDto.class,
                couponTemplate.couponTemplateId,
                couponPolicy.discountPrice,
                couponPolicy.condition,
                couponPolicy.maxPrice,
                couponPolicy.duration,
                couponTemplate.couponName,
                couponTemplate.couponDescription))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();
    }
}
