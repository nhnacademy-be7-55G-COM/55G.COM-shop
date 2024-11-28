package shop.s5g.shop.service.coupon.coupon.impl;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.coupon.coupon.AvailableCouponResponseDto;
import shop.s5g.shop.dto.coupon.coupon.CouponRequestDto;
import shop.s5g.shop.dto.coupon.coupon.CouponResponseDto;
import shop.s5g.shop.entity.coupon.Coupon;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.entity.coupon.CouponTemplate.CouponTemplateType;
import shop.s5g.shop.exception.coupon.CouponAlreadyDeletedException;
import shop.s5g.shop.exception.coupon.CouponNotFoundException;
import shop.s5g.shop.exception.coupon.CouponTemplateNotFoundException;
import shop.s5g.shop.repository.coupon.coupon.CouponRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;
import shop.s5g.shop.service.coupon.coupon.CouponService;
import shop.s5g.shop.util.CouponUtil;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponTemplateRepository couponTemplateRepository;

    /**
     * 웰컴 쿠폰 생성
     * @return Coupon
     */
    @Override
    public Coupon createWelcomeCoupon() {

        CouponTemplate welcomeTemplate = couponTemplateRepository.findParticularCouponByName(
            CouponTemplateType.WELCOME.getTypeName());

        if (Objects.isNull(welcomeTemplate)) {
            return null;
        }

        return couponRepository.save(
            new Coupon(
                welcomeTemplate,
                CouponUtil.createUniqueCouponNumber()
            )
        );
    }


    /**
     * 쿠폰 조회
     * @param couponCode
     * @return Coupon
     */
    @Override
    public Coupon getCouponByCode(String couponCode) {

        return couponRepository.findByCouponCode(couponCode);
    }

    /**
     * 특정 쿠폰 조회
     * @param couponId
     * @return CouponResponseDto
     */
    @Override
    public CouponResponseDto getCoupon(Long couponId) {

        if (Objects.isNull(couponId) || couponId < 0) {
            throw new IllegalArgumentException("쿠폰 아이디가 잘못 지정되었습니다.");
        }

        CouponResponseDto couponResponseDto = couponRepository.findCoupon(couponId);

        if (Objects.isNull(couponResponseDto)) {
            throw new CouponNotFoundException("선택하신 쿠폰을 찾으실 수 없습니다.");
        }

        return couponResponseDto;
    }

    /**
     * 발급한 모든 쿠폰 조회
     * @param pageable
     * @return Page<CouponResponseDto> - paging 처리
     */
    @Override
    public Page<CouponResponseDto> getAllCouponList(Pageable pageable) {
        return couponRepository.getAllIssuedCoupons(pageable);
    }

    /**
     * 쿠폰 삭제
     * @param couponId
     */
    @Override
    public void deleteCoupon(Long couponId) {
        if (Objects.isNull(couponId) || couponId <= 0) {
            throw new IllegalArgumentException("쿠폰 아이디의 값이 잘못 지정되었습니다.");
        }

        if (!couponRepository.existsById(couponId)) {
            throw new CouponNotFoundException("해당 쿠폰은 존재하지 않는 쿠폰입니다.");
        }

        if (!couponRepository.checkActiveCoupon(couponId)) {
            throw new CouponAlreadyDeletedException("해당 쿠폰은 삭제된 쿠폰입니다.");
        }

        couponRepository.deleteCouponById(couponId);
    }

    /**
     * 발급 가능한 쿠폰 조회
     * @param pageable
     * @return Page<AvailableCouponResponseDto>
     */
    @Override
    public Page<AvailableCouponResponseDto> getAvailableCoupons(Pageable pageable) {

        return couponRepository.getAllAvailableCoupons(pageable);
    }

}
