package shop.s5g.shop.service.coupon.policy.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.coupon.policy.CouponPolicyRequestDto;
import shop.s5g.shop.dto.coupon.policy.CouponPolicyResponseDto;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.exception.ErrorCode;
import shop.s5g.shop.exception.coupon.CouponPolicyNotFoundException;
import shop.s5g.shop.exception.coupon.CouponPolicyValidationException;
import shop.s5g.shop.repository.coupon.policy.CouponPolicyRepository;
import shop.s5g.shop.service.coupon.policy.CouponPolicyService;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponPolicyServiceImpl implements CouponPolicyService {

    private final CouponPolicyRepository couponPolicyRepository;

    /**
     * 쿠폰 정책 생성
     * @param couponPolicyRequestDto
     * @return couponPolicy entity
     */
    @Override
    public CouponPolicy saveCouponPolicy(CouponPolicyRequestDto couponPolicyRequestDto) {

        validateCouponPolicy(couponPolicyRequestDto);

        CouponPolicy couponPolicy = new CouponPolicy(
            couponPolicyRequestDto.discountPrice(),
            couponPolicyRequestDto.condition(),
            couponPolicyRequestDto.maxPrice(),
            couponPolicyRequestDto.duration()
        );

        return couponPolicyRepository.save(couponPolicy);
    }

    /**
     * 쿠폰 정책 업데이트
     * @param couponPolicyId
     * @param couponPolicyRequestDto
     */
    @Override
    public void updateCouponPolicy(Long couponPolicyId, CouponPolicyRequestDto couponPolicyRequestDto) {

        //TODO (young) : 에러 처리 수정 예정
        if (Objects.isNull(couponPolicyId) || couponPolicyId <= 0) {
            throw new IllegalArgumentException();
        }

        if (!couponPolicyRepository.existsById(couponPolicyId)) {
            throw new CouponPolicyNotFoundException(couponPolicyId + ", 아이디는 존재하지 않는 쿠폰 정책입니다");
        }

        validateCouponPolicy(couponPolicyRequestDto);

        couponPolicyRepository.updateCouponPolicy(
            couponPolicyId,
            couponPolicyRequestDto.discountPrice(),
            couponPolicyRequestDto.condition(),
            couponPolicyRequestDto.maxPrice(),
            couponPolicyRequestDto.duration()
        );
    }

    /**
     * 특정 쿠폰 정책 찾기
     * @param couponPolicyId
     * @return couponPolicyResponseDto
     */
    @Override
    @Transactional(readOnly = true)
    public CouponPolicyResponseDto getByCouponPolicyId(Long couponPolicyId) {

        //TODO (young) : 에러 처리 수정 예정
        if (Objects.isNull(couponPolicyId) || couponPolicyId <= 0) {
            throw new IllegalArgumentException();
        }

        CouponPolicy couponPolicy = couponPolicyRepository.findById(couponPolicyId)
                                            .orElseThrow(() -> new CouponPolicyNotFoundException(couponPolicyId + ", 아이디는 존재하지 않는 쿠폰 정책입니다"));

        return new CouponPolicyResponseDto(
            couponPolicy.getCouponPolicyId(),
            couponPolicy.getDiscountPrice(),
            couponPolicy.getCondition(),
            couponPolicy.getMaxPrice(),
            couponPolicy.getDuration()
        );
    }

    /**
     * 모든 쿠폰 정책 찾기
     * @return Page<CouponPolicyResponseDto>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CouponPolicyResponseDto> getAllCouponPolices(Pageable pageable) {

        return couponPolicyRepository.findAllCouponPolicies(pageable);
    }

    /**
     * request 데이터 유효성 검사
     * @param couponPolicyRequestDto
     */
    private void validateCouponPolicy(CouponPolicyRequestDto couponPolicyRequestDto) {

        BigDecimal discountPrice = couponPolicyRequestDto.discountPrice();
        Long condition = couponPolicyRequestDto.condition();
        Long maxPrice = couponPolicyRequestDto.maxPrice();

        //TODO (young) : 에러 처리 수정 예정
        if (discountPrice.compareTo(BigDecimal.ONE) < 0) {
            if (discountPrice.compareTo(new BigDecimal("0.8")) > 0) {
                throw new CouponPolicyValidationException(ErrorCode.DISCOUNT_EXCEEDS_80_PERCENT);
            }
        } else {
            if (discountPrice.compareTo(BigDecimal.valueOf(1000)) < 0 || discountPrice.compareTo(BigDecimal.valueOf(condition).multiply(BigDecimal.valueOf(0.8))) > 0) {
                throw new CouponPolicyValidationException(ErrorCode.DISCOUNT_INVALID_RANGE);
            }
        }

        if (Objects.nonNull(maxPrice)) {
            if (maxPrice > (condition / 2)) {
                throw new CouponPolicyValidationException(ErrorCode.MAX_PRICE_EXCEEDS_LIMIT);
            }
        }
    }
}
