package shop.S5G.shop.service.coupon.policy.impl;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.coupon.policy.CouponPolicyRequestDto;
import shop.S5G.shop.dto.coupon.policy.CouponPolicyResponseDto;
import shop.S5G.shop.entity.coupon.CouponPolicy;
import shop.S5G.shop.exception.coupon.CouponPolicyNotFoundException;
import shop.S5G.shop.repository.coupon.policy.CouponPolicyRepository;
import shop.S5G.shop.service.coupon.policy.CouponPolicyService;

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
        if (Objects.isNull(couponPolicyId) || couponPolicyId <= 0) {
            throw new IllegalArgumentException();
        }

        if (!couponPolicyRepository.existsById(couponPolicyId)) {
            throw new CouponPolicyNotFoundException(couponPolicyId + ", 아이디는 존재하지 않는 쿠폰 정책입니다");
        }

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
    public CouponPolicyResponseDto findByCouponPolicyId(Long couponPolicyId) {

        if (Objects.isNull(couponPolicyId) || couponPolicyId <= 0) {
            throw new IllegalArgumentException();
        }

        CouponPolicy couponPolicy = couponPolicyRepository.findById(couponPolicyId)
                                            .orElseThrow(() -> new CouponPolicyNotFoundException(couponPolicyId + ", 아이디는 존재하지 않는 쿠폰 정책입니다"));

        return new CouponPolicyResponseDto(
            couponPolicy.getDiscountPrice(),
            couponPolicy.getCondition(),
            couponPolicy.getMaxPrice(),
            couponPolicy.getDuration()
        );
    }

    /**
     * 모든 쿠폰 정책 찾기
     * @return List<CouponPolicyResponseDto>
     */
    @Override
    @Transactional(readOnly = true)
    public List<CouponPolicyResponseDto> findByAllCouponPolicies() {

        return couponPolicyRepository.findAll()
            .stream()
            .map(couponPolicy -> new CouponPolicyResponseDto(
                couponPolicy.getDiscountPrice(),
                couponPolicy.getCondition(),
                couponPolicy.getMaxPrice(),
                couponPolicy.getDuration()
            ))
            .toList();
    }
}
