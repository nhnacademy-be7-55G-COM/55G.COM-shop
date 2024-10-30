package shop.S5G.shop.service.coupon.template.impl;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.S5G.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.S5G.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.S5G.shop.entity.coupon.CouponTemplate;
import shop.S5G.shop.exception.coupon.CouponPolicyNotFoundException;
import shop.S5G.shop.exception.coupon.CouponTemplateNotFoundException;
import shop.S5G.shop.repository.coupon.policy.CouponPolicyRepository;
import shop.S5G.shop.repository.coupon.template.CouponTemplateRepository;
import shop.S5G.shop.service.coupon.template.CouponTemplateService;

@Service
@RequiredArgsConstructor
public class CouponTemplateServiceImpl implements CouponTemplateService {

    private final CouponTemplateRepository couponTemplateRepository;

    private final CouponPolicyRepository couponPolicyRepository;

    /**
     * 쿠폰 템플릿 생성
     * @param couponTemplateRequestDto
     */
    @Override
    public void createCouponTemplate(CouponTemplateRequestDto couponTemplateRequestDto) {

        if (!couponPolicyRepository.existsById(couponTemplateRequestDto.couponPolicy().getCouponPolicyId())) {
            throw new CouponPolicyNotFoundException("선택하신 쿠폰 정책이 존재하지 않습니다.");
        }

        CouponTemplate couponTemplate = new CouponTemplate(
            couponTemplateRequestDto.couponPolicy(),
            couponTemplateRequestDto.couponName(),
            couponTemplateRequestDto.couponDescription()
        );

        couponTemplateRepository.save(couponTemplate);
    }

    /**
     * 특정 쿠폰 정책 조회
     * @param couponTemplateId
     * @return couponTemplateResponseDto
     */
    @Override
    public CouponTemplateResponseDto findCouponTemplate(Long couponTemplateId) {

        if (Objects.isNull(couponTemplateId) || couponTemplateId <= 0) {
            throw new IllegalArgumentException();
        }

        if (!couponTemplateRepository.existsById(couponTemplateId)) {
            throw new CouponTemplateNotFoundException("쿠폰 템플릿이 존재하지 않습니다.");
        }

        return couponTemplateRepository.findCouponTemplateById(couponTemplateId);
    }

    /**
     * 쿠폰 템플릿 수정
     * @param couponTemplateId
     * @param couponTemplateRequestDto
     */
    @Override
    public void updateCouponTemplate(Long couponTemplateId, CouponTemplateRequestDto couponTemplateRequestDto) {

        if (Objects.isNull(couponTemplateId) || couponTemplateId <= 0) {
            throw new IllegalArgumentException();
        }

        if (!couponTemplateRepository.existsById(couponTemplateId)) {
            throw new CouponTemplateNotFoundException("쿠폰 템플릿이 존재하지 않습니다.");
        }

        if (!couponTemplateRepository.checkActiveCouponTemplate(couponTemplateId)) {
            throw new CouponTemplateNotFoundException("삭제된 쿠폰 템플릿입니다.");
        }

        if (!couponPolicyRepository.existsById(couponTemplateRequestDto.couponPolicy().getCouponPolicyId())) {
            throw new CouponPolicyNotFoundException("선택하신 쿠폰 정책이 존재하지 않습니다.");
        }

        couponTemplateRepository.updateCouponTemplate(couponTemplateId, couponTemplateRequestDto);
    }

    /**
     * 쿠폰 템플릿 삭제
     * @param couponTemplateId
     */
    @Override
    public void deleteCouponTemplate(Long couponTemplateId) {

        if (Objects.isNull(couponTemplateId) || couponTemplateId <= 0) {
            throw new IllegalArgumentException();
        }

        if (!couponTemplateRepository.existsById(couponTemplateId)) {
            throw new CouponTemplateNotFoundException("쿠폰 템플릿이 존재하지 않습니다.");
        }

        if (!couponTemplateRepository.checkActiveCouponTemplate(couponTemplateId)) {
            throw new CouponTemplateNotFoundException("이미 삭제된 쿠폰 템플릿입니다.");
        }

        couponTemplateRepository.deleteCouponTemplate(couponTemplateId);
    }
}
