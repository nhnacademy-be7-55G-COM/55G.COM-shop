package shop.S5G.shop.repository.coupon.coupon.qdsl;

import static shop.S5G.shop.entity.coupon.QCoupon.coupon;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.S5G.shop.dto.coupon.coupon.CouponRequestDto;
import shop.S5G.shop.dto.coupon.coupon.CouponResponseDto;
import shop.S5G.shop.entity.coupon.Coupon;

public class CouponQueryRepositoryImpl extends QuerydslRepositorySupport implements CouponQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CouponQueryRepositoryImpl(EntityManager em) {
        super(Coupon.class);
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @Override
    public CouponResponseDto createCoupon(CouponRequestDto couponRequestDto) {
        return null;
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
        return null;
    }

    @Override
    public List<CouponResponseDto> findCoupons(Pageable pageable) {
        return List.of();
    }

    @Override
    public void deleteCoupon(Long couponId) {

    }
}
