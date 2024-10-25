package shop.S5G.shop.service.coupon.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.couponpolicy.CouponPolicyRequestDto;
import shop.S5G.shop.dto.couponpolicy.CouponPolicyResponseDto;
import shop.S5G.shop.entity.coupon.CouponPolicy;
import shop.S5G.shop.exception.coupon.CouponPolicyNotFoundException;
import shop.S5G.shop.repository.coupon.CouponPolicyRepository;
import shop.S5G.shop.service.coupon.CouponPolicyService;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponPolicyServiceImpl implements CouponPolicyService {

    private final CouponPolicyRepository couponPolicyRepository;

    @Override
    public void saveCouponPolicy(CouponPolicyRequestDto couponPolicyRequestDto) {

        CouponPolicy couponPolicy = new CouponPolicy(
            couponPolicyRequestDto.discountPrice(),
            couponPolicyRequestDto.condition(),
            couponPolicyRequestDto.maxPrice(),
            couponPolicyRequestDto.duration()
        );

        couponPolicyRepository.save(couponPolicy);
    }

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
