package shop.s5g.shop.service.coupon.coupon.impl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.coupon.coupon.CouponRequestDto;
import shop.s5g.shop.dto.coupon.coupon.CouponResponseDto;
import shop.s5g.shop.entity.coupon.Coupon;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.exception.coupon.CouponAlreadyDeletedException;
import shop.s5g.shop.exception.coupon.CouponNotFoundException;
import shop.s5g.shop.exception.coupon.CouponTemplateNotFoundException;
import shop.s5g.shop.repository.coupon.coupon.CouponRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;
import shop.s5g.shop.service.coupon.coupon.CouponService;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponTemplateRepository couponTemplateRepository;

    /**
     * 쿠폰 생성 ( 1 ~ n개 생성 가능 )
     * @param couponCnt
     * @param couponRequestDto
     */
    @Override
    public void createCoupon(Integer couponCnt, CouponRequestDto couponRequestDto) {

        if (Objects.isNull(couponCnt) || couponCnt <= 0) {
            throw new IllegalArgumentException("쿠폰 수량이 잘못 지정되었습니다.");
        }

        CouponTemplate couponTemplate = couponTemplateRepository.findById(couponRequestDto.couponTemplateId())
            .orElseThrow(() -> new CouponTemplateNotFoundException("쿠폰 템플릿을 찾을 수 없습니다."));

        Set<Coupon> couponSet = new HashSet<>();

        while (couponSet.size() < couponCnt) {
            couponSet.add(
                new Coupon(
                    couponTemplate,
                    createCouponNumber(),
                    couponRequestDto.expiredAt()
                )
            );
        }

        couponRepository.saveAll(couponSet);
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
     * 쿠폰 수정
     * @param couponId
     */
    @Override
    public void updateCoupon(Long couponId, LocalDateTime expiredAt) {

        if (Objects.isNull(couponId) || couponId < 0) {
            throw new IllegalArgumentException("쿠폰 아이디가 잘못 지정되었습니다.");
        }

        if (!couponRepository.existsById(couponId)) {
            throw new CouponNotFoundException("해당 쿠폰이 존재하지 않습니다.");
        }

        if (!couponRepository.checkActiveCoupon(couponId)) {
            throw new CouponAlreadyDeletedException("해당 쿠폰은 삭제된 쿠폰입니다.");
        }

        couponRepository.updateCouponExpiredDatetime(couponId, expiredAt);
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
     * 쿠폰 번호 랜덤 생성
     * @return String
     */
    private String createCouponNumber() {
        return RandomStringUtils.randomAlphanumeric(15);
    }
}
