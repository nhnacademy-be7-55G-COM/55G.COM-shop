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
import shop.s5g.shop.dto.coupon.user.UserCouponResponseDto;
import shop.s5g.shop.entity.coupon.QCoupon;
import shop.s5g.shop.entity.coupon.QCouponTemplate;
import shop.s5g.shop.entity.coupon.QUserCoupon;
import shop.s5g.shop.entity.coupon.UserCoupon;
import shop.s5g.shop.entity.member.QCustomer;

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
    QCustomer customer = QCustomer.customer;

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
     * @return Page<UserCouponResponseDto>
     */
    @Override
    public Page<UserCouponResponseDto> findUnusedCouponList(Long customerId, Pageable pageable) {

        List<UserCouponResponseDto> userCouponList = queryFactory
            .select(Projections.constructor(UserCouponResponseDto.class,
                coupon.couponId,
                coupon.couponCode,
                coupon.createdAt,
                coupon.expiredAt,
                couponTemplate.couponName,
                couponTemplate.couponDescription))
            .from(userCoupon)
            .where(userCoupon.userCouponPk.customerId.eq(customerId))
            .innerJoin(coupon)
            .on(userCoupon.userCouponPk.couponId.eq(coupon.couponId))
            .where(coupon.active.eq(true)
                .and(coupon.expiredAt.after(LocalDateTime.now()))
                .and(coupon.usedAt.isNull()))
            .innerJoin(couponTemplate)
            .on(coupon.couponTemplate.couponTemplateId.eq(couponTemplate.couponTemplateId))
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
     * @return
     */
    @Override
    public Page<UserCouponResponseDto> findUsedCouponList(Long customerId, Pageable pageable) {
        List<UserCouponResponseDto> userCouponList = queryFactory
            .select(Projections.constructor(UserCouponResponseDto.class,
                coupon.couponId,
                coupon.couponCode,
                coupon.createdAt,
                coupon.expiredAt,
                couponTemplate.couponName,
                couponTemplate.couponDescription))
            .from(userCoupon)
            .where(userCoupon.userCouponPk.customerId.eq(customerId))
            .innerJoin(coupon)
            .on(userCoupon.userCouponPk.couponId.eq(coupon.couponId))
            .where(coupon.expiredAt.before(LocalDateTime.now())
                .and(coupon.usedAt.isNull()))
            .innerJoin(couponTemplate)
            .on(coupon.couponTemplate.couponTemplateId.eq(couponTemplate.couponTemplateId))
            .fetch();

        Long totalCnt = queryFactory
            .select(userCoupon.count())
            .from(userCoupon)
            .fetchOne();

        long total = (Objects.isNull(totalCnt)) ? 0L : totalCnt;

        return new PageImpl<>(userCouponList, pageable, total);
    }
}
