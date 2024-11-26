package shop.s5g.shop.repository.coupon.user.qdsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.coupon.user.InValidUsedCouponResponseDto;
import shop.s5g.shop.dto.coupon.user.ValidUserCouponResponseDto;
import shop.s5g.shop.entity.coupon.QCoupon;
import shop.s5g.shop.entity.coupon.QCouponPolicy;
import shop.s5g.shop.entity.coupon.QCouponTemplate;
import shop.s5g.shop.entity.coupon.QUserCoupon;
import shop.s5g.shop.entity.coupon.UserCoupon;

public class UserCouponQuerydslRepositoryImpl extends QuerydslRepositorySupport implements
    UserCouponQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public UserCouponQuerydslRepositoryImpl(JPAQueryFactory queryFactory) {
        super(UserCoupon.class);
        this.queryFactory = queryFactory;
    }

    QUserCoupon userCoupon = QUserCoupon.userCoupon;
    QCoupon coupon = QCoupon.coupon;
    QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;
    QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;

    /**
     * 해당 유저가 쿠폰 템플릿을 가지고 있는 지 체크하기
     * @param memberId
     * @param couponTemplateName
     * @return boolean - 쿠폰 소유 여부
     */
    @Override
    public boolean userHasCouponTemplate(Long memberId, String couponTemplateName) {

        return queryFactory
            .selectOne()
            .from(userCoupon)
            .where(userCoupon.userCouponPk.customerId.eq(memberId))
            .innerJoin(coupon)
            .on(userCoupon.userCouponPk.couponId.eq(coupon.couponId))
            .innerJoin(couponTemplate)
            .on(coupon.couponTemplate.couponTemplateId.eq(couponTemplate.couponTemplateId))
            .where(Expressions.stringTemplate("locate({0}, {1})", couponTemplateName, couponTemplate.couponName).gt("0"))
            .fetchOne() != null;
    }

    /**
     * 사용하지 않은 coupon 리스트 가져오기
     * @param customerId
     * @param pageable
     * @return Page<ValidUserCouponResponseDto>
     */
    @Override
    public Page<ValidUserCouponResponseDto> findUnusedCouponList(Long customerId, Pageable pageable) {

        LocalDateTime now = LocalDateTime.now();

        List<ValidUserCouponResponseDto> userCouponList = queryFactory
            .select(Projections.constructor(ValidUserCouponResponseDto.class,
                coupon.couponId,
                couponTemplate.couponTemplateId,
                coupon.couponCode,
                couponTemplate.couponName,
                coupon.createdAt,
                coupon.expiredAt,
                couponTemplate.couponDescription,
                couponPolicy.condition,
                couponPolicy.discountPrice,
                couponPolicy.maxPrice
            ))
            .from(userCoupon)
            .innerJoin(coupon).on(userCoupon.userCouponPk.couponId.eq(coupon.couponId))
            .innerJoin(couponTemplate).on(coupon.couponTemplate.couponTemplateId.eq(couponTemplate.couponTemplateId))
            .innerJoin(couponPolicy).on(couponTemplate.couponPolicy.couponPolicyId.eq(couponPolicy.couponPolicyId))
            .where(
                userCoupon.userCouponPk.customerId.eq(customerId)
                    .and(coupon.active.isTrue())
                    .and(coupon.expiredAt.after(now))
                    .and(coupon.usedAt.isNull())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long totalCnt = queryFactory
            .select(userCoupon.count())
            .from(userCoupon)
            .fetchOne();

        long total = (Objects.isNull(totalCnt)) ? 0L : totalCnt;

        return new PageImpl<>(userCouponList, pageable, total);
    }

    /**
     * 사용한 쿠폰 리스트 가져오기
     * @param customerId
     * @param pageable
     * @return Page<InValidUsedCouponResponseDto>
     */
    @Override
    public Page<InValidUsedCouponResponseDto> findUsedCouponList(Long customerId,
        Pageable pageable) {

        List<InValidUsedCouponResponseDto> usedCouponList = queryFactory
            .select(Projections.constructor(InValidUsedCouponResponseDto.class,
                coupon.couponId,
                coupon.couponCode,
                couponTemplate.couponName,
                coupon.createdAt,
                coupon.expiredAt,
                coupon.usedAt,
                couponTemplate.couponDescription,
                couponPolicy.condition,
                couponPolicy.discountPrice,
                couponPolicy.maxPrice))
            .from(userCoupon)
            .innerJoin(coupon).on(userCoupon.userCouponPk.couponId.eq(coupon.couponId))
            .innerJoin(couponTemplate).on(coupon.couponTemplate.couponTemplateId.eq(couponTemplate.couponTemplateId))
            .innerJoin(couponPolicy).on(couponTemplate.couponPolicy.couponPolicyId.eq(couponPolicy.couponPolicyId))
            .where(
                userCoupon.userCouponPk.customerId.eq(customerId)
                    .and(coupon.active.isFalse())
                    .and(coupon.usedAt.isNotNull())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long totalCnt = queryFactory
            .select(userCoupon.count())
            .from(userCoupon)
            .fetchOne();

        long total = (Objects.isNull(totalCnt)) ? 0L : totalCnt;

        return new PageImpl<>(usedCouponList, pageable, total);
    }

    /**
     * 사용하지는 않았으나 기간이 만료된 쿠폰 리스트 가져오기
     * @param customerId
     * @param pageable
     * @return Page<InValidUsedCouponResponseDto>
     */
    @Override
    public Page<InValidUsedCouponResponseDto> findAfterExpiredUserCouponList(Long customerId,
        Pageable pageable) {

        LocalDateTime now = LocalDateTime.now();

        List<InValidUsedCouponResponseDto> expiredCouponList = queryFactory
            .select(Projections.constructor(InValidUsedCouponResponseDto.class,
                coupon.couponId,
                coupon.couponCode,
                couponTemplate.couponName,
                coupon.createdAt,
                coupon.expiredAt,
                coupon.usedAt,
                couponTemplate.couponDescription,
                couponPolicy.condition,
                couponPolicy.discountPrice,
                couponPolicy.maxPrice))
            .from(userCoupon)
            .innerJoin(coupon).on(userCoupon.userCouponPk.couponId.eq(coupon.couponId))
            .innerJoin(couponTemplate).on(coupon.couponTemplate.couponTemplateId.eq(couponTemplate.couponTemplateId))
            .innerJoin(couponPolicy).on(couponTemplate.couponPolicy.couponPolicyId.eq(couponPolicy.couponPolicyId))
            .where(
                userCoupon.userCouponPk.customerId.eq(customerId)
                    .and(coupon.active.isFalse())
                    .and(coupon.usedAt.isNull())
                    .and(coupon.expiredAt.before(now))
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long totalCnt = queryFactory
            .select(userCoupon.count())
            .from(userCoupon)
            .fetchOne();

        long total = (Objects.isNull(totalCnt)) ? 0L : totalCnt;

        return new PageImpl<>(expiredCouponList, pageable, total);
    }

    /**
     * 사용하지 않아서 기간이 만료되었거나, 사용한 쿠폰 리스트 가져오기
     * @param customerId
     * @param pageable
     * @return Page<InValidUsedCouponResponseDto>
     */
    @Override
    public Page<InValidUsedCouponResponseDto> findInvalidUserCouponList(Long customerId,
        Pageable pageable) {

        LocalDateTime now = LocalDateTime.now();

        List<InValidUsedCouponResponseDto> invalidCouponList = queryFactory
            .select(Projections.constructor(InValidUsedCouponResponseDto.class,
                coupon.couponId,
                coupon.couponCode,
                couponTemplate.couponName,
                coupon.createdAt,
                coupon.expiredAt,
                coupon.usedAt,
                couponTemplate.couponDescription,
                couponPolicy.condition,
                couponPolicy.discountPrice,
                couponPolicy.maxPrice))
            .from(userCoupon)
            .innerJoin(coupon).on(userCoupon.userCouponPk.couponId.eq(coupon.couponId))
            .innerJoin(couponTemplate).on(coupon.couponTemplate.couponTemplateId.eq(couponTemplate.couponTemplateId))
            .innerJoin(couponPolicy).on(couponTemplate.couponPolicy.couponPolicyId.eq(couponPolicy.couponPolicyId))
            .where(
                userCoupon.userCouponPk.customerId.eq(customerId)
                    .and(
                        coupon.usedAt.isNull().and(coupon.expiredAt.before(now))
                            .or(coupon.usedAt.isNotNull())
                    )
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long totalCnt = queryFactory
            .select(userCoupon.count())
            .from(userCoupon)
            .fetchOne();

        long total = (Objects.isNull(totalCnt)) ? 0L : totalCnt;

        return new PageImpl<>(invalidCouponList, pageable, total);
    }
}
