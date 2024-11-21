package shop.s5g.shop.repository.coupon.category.qdsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.coupon.category.CouponCategoryDetailsForCategoryDto;
import shop.s5g.shop.dto.coupon.category.CouponCategoryResponseDto;
import shop.s5g.shop.entity.QCategory;
import shop.s5g.shop.entity.coupon.CouponCategory;
import shop.s5g.shop.entity.coupon.QCouponCategory;
import shop.s5g.shop.entity.coupon.QCouponPolicy;
import shop.s5g.shop.entity.coupon.QCouponTemplate;

public class CouponCategoryQuerydslRepositoryImpl extends QuerydslRepositorySupport implements CouponCategoryQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    private static final boolean ACTIVE = true;

    public CouponCategoryQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(CouponCategory.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    QCouponCategory couponCategory = QCouponCategory.couponCategory;
    QCategory category = QCategory.category;
    QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;
    QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;

    /**
     * 카테고리 쿠폰이 존재여부 체크
     * @param couponTemplateId
     * @param categoryId
     * @return boolean
     */
    @Override
    public boolean existsByCouponTemplateAndCategory(Long couponTemplateId, Long categoryId) {
        return !jpaQueryFactory
            .selectFrom(couponCategory)
            .where(couponCategory.category.categoryId.eq(categoryId)
                .and(couponCategory.couponTemplate.couponTemplateId.eq(couponTemplateId)))
            .fetch()
            .isEmpty();
    }

    /**
     * 모든 카테고리 쿠폰 조회 - Pageable
     * @param pageable
     * @return Page<CouponCategoryResponseDto>
     */
    @Override
    public Page<CouponCategoryResponseDto> findAllCouponCategories(Pageable pageable) {

        List<CouponCategoryResponseDto> couponCategoryList = jpaQueryFactory
            .select(Projections.constructor(CouponCategoryResponseDto.class,
                category.categoryId,
                category.categoryName,
                couponPolicy.discountPrice,
                couponPolicy.condition,
                couponPolicy.maxPrice,
                couponPolicy.duration,
                couponTemplate.couponName,
                couponTemplate.couponDescription))
            .from(couponCategory)
            .innerJoin(category)
            .on(couponCategory.category.categoryId.eq(category.categoryId))
            .innerJoin(couponTemplate)
            .on(couponCategory.couponTemplate.couponTemplateId.eq(couponTemplate.couponTemplateId))
            .innerJoin(couponPolicy)
            .on(couponTemplate.couponPolicy.couponPolicyId.eq(couponPolicy.couponPolicyId))
            .where(couponTemplate.active.eq(ACTIVE))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        Long totalCnt = jpaQueryFactory
            .select(couponCategory.count())
            .from(couponCategory)
            .fetchOne();

        long total = (Objects.nonNull(totalCnt)) ? totalCnt : 0L;

        return new PageImpl<>(couponCategoryList, pageable, total);
    }

    /**
     * 쿠폰 적용이 된 모든 카테고리 조회 - Pageable
     * @param pageable
     * @return Page<CouponCategoryDetailsForCategoryDto>
     */
    @Override
    public Page<CouponCategoryDetailsForCategoryDto> findCategoryInfoByCouponTemplate(Pageable pageable) {

        List<CouponCategoryDetailsForCategoryDto> categoryNames = jpaQueryFactory
            .select(Projections.constructor(CouponCategoryDetailsForCategoryDto.class,
                category.categoryId,
                category.categoryName))
            .from(couponCategory)
            .innerJoin(category)
            .on(couponCategory.category.categoryId.eq(category.categoryId))
            .innerJoin(couponTemplate)
            .on(couponCategory.couponTemplate.couponTemplateId.eq(couponTemplate.couponTemplateId))
            .where(couponTemplate.active.eq(ACTIVE))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        Long totalCnt = jpaQueryFactory
            .select(couponCategory.count())
            .from(couponCategory)
            .fetchOne();

        long total = (Objects.nonNull(totalCnt)) ? totalCnt : 0L;

        return new PageImpl<>(categoryNames, pageable, total);
    }

}
