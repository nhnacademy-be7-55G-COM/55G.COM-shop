package shop.s5g.shop.repository.coupon.template.qdsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

    QCoupon coupon = QCoupon.coupon;
    QCouponBook couponBook = QCouponBook.couponBook;
    QCouponCategory couponCategory = QCouponCategory.couponCategory;
    QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;
    QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;

    /**
     * 특정 쿠폰 템플릿 찾기 쿼리 dsl
     * @param couponTemplateId
     * @return CouponTemplateResponseDto
     */
    @Override
    public CouponTemplateResponseDto findCouponTemplateById(Long couponTemplateId) {

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
     * @return Page<CouponTemplateResponseDto>
     */
    @Override
    public Page<CouponTemplateResponseDto> findCouponTemplatesByPageable(Pageable pageable) {

        List<CouponTemplateResponseDto> templateList = jpaQueryFactory
            .select(Projections.constructor(CouponTemplateResponseDto.class,
                couponTemplate.couponTemplateId,
                couponPolicy.discountPrice,
                couponPolicy.condition,
                couponPolicy.maxPrice,
                couponPolicy.duration,
                couponTemplate.couponName,
                couponTemplate.couponDescription))
            .from(couponTemplate)
            .innerJoin(couponPolicy)
            .on(couponTemplate.couponPolicy.couponPolicyId.eq(couponPolicy.couponPolicyId))
            .where(couponTemplate.active.eq(ACTIVE))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        Long totalCnt = jpaQueryFactory
            .select(couponTemplate.count())
            .from(couponTemplate)
            .fetchOne();

        long total = (Objects.isNull(totalCnt)) ? 0L : totalCnt;

        return new PageImpl<>(templateList, pageable, total);
    }

    /**
     * 도서와 카테고리에 쓰이지 않은 쿠폰 템플릿 조회
     * @param pageable
     * @return Page<CouponTemplateResponseDto>
     */
    @Override
    public Page<CouponTemplateResponseDto> findUnusedCouponTemplates(Pageable pageable) {

        List<CouponTemplateResponseDto> templateList = jpaQueryFactory
            .select(Projections.constructor(CouponTemplateResponseDto.class,
                couponTemplate.couponTemplateId,
                couponPolicy.discountPrice,
                couponPolicy.condition,
                couponPolicy.maxPrice,
                couponPolicy.duration,
                couponTemplate.couponName,
                couponTemplate.couponDescription))
            .from(couponTemplate)
            .leftJoin(couponCategory)
            .on(couponTemplate.couponTemplateId.eq(couponCategory.couponTemplateId))
            .leftJoin(couponBook)
            .on(couponTemplate.couponTemplateId.eq(couponBook.couponTemplateId))
            .where(couponCategory.couponTemplateId.isNull()
                .and(couponBook.couponTemplateId.isNull())
                .and(couponTemplate.active.eq(ACTIVE)))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        Long totalCnt = jpaQueryFactory
            .select(couponTemplate.count())
            .from(couponTemplate)
            .fetchOne();

        long total = (Objects.isNull(totalCnt)) ? 0L : totalCnt;

        return new PageImpl<>(templateList, pageable, total);
    }
}
