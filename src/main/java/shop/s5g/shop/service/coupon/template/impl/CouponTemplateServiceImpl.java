package shop.s5g.shop.service.coupon.template.impl;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.coupon.template.CouponCategoryAndBookResponseDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateUpdateRequestDto;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.entity.coupon.CouponTemplate.CouponTemplateType;
import shop.s5g.shop.exception.coupon.CouponPolicyNotFoundException;
import shop.s5g.shop.exception.coupon.CouponTemplateAlreadyExistsException;
import shop.s5g.shop.exception.coupon.CouponTemplateNotFoundException;
import shop.s5g.shop.repository.coupon.policy.CouponPolicyRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;
import shop.s5g.shop.service.coupon.template.CouponTemplateService;

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
    @Transactional
    public void createCouponTemplate(CouponTemplateRequestDto couponTemplateRequestDto) {

        if (couponTemplateRequestDto.couponName().contains(CouponTemplateType.WELCOME.getTypeName())
        || couponTemplateRequestDto.couponName().contains(CouponTemplateType.BIRTH.getTypeName())) {
            String couponType = couponTemplateRequestDto.couponName().split(" ")[0];

            if (couponTemplateRepository.existsCouponTemplateName(couponType)) {
                throw new CouponTemplateAlreadyExistsException("해당 쿠폰 템플릿은 이미 존재합니다.");
            }

        }

        CouponPolicy couponPolicy = couponPolicyRepository.findById(couponTemplateRequestDto.couponPolicyId())
            .orElseThrow(() -> new CouponPolicyNotFoundException("선택하신 쿠폰 정책이 존재하지 않습니다."));

        CouponTemplate couponTemplate = new CouponTemplate(
            couponPolicy,
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
    @Transactional(readOnly = true)
    public CouponTemplateResponseDto getCouponTemplate(Long couponTemplateId) {

        if (Objects.isNull(couponTemplateId) || couponTemplateId <= 0) {
            throw new IllegalArgumentException();
        }

        if (!couponTemplateRepository.existsById(couponTemplateId)) {
            throw new CouponTemplateNotFoundException("쿠폰 템플릿이 존재하지 않습니다.");
        }

        if (!couponTemplateRepository.checkActiveCouponTemplate(couponTemplateId)) {
            throw new CouponPolicyNotFoundException("삭제된 쿠폰 템플릿입니다.");
        }

        return couponTemplateRepository.findCouponTemplateById(couponTemplateId);
    }

    /**
     * 쿠폰 템플릿 수정
     * @param couponTemplateId
     * @param couponTemplateUpdateRequestDto
     */
    @Override
    @Transactional
    public void updateCouponTemplate(Long couponTemplateId, CouponTemplateUpdateRequestDto couponTemplateUpdateRequestDto) {

        if (Objects.isNull(couponTemplateId) || couponTemplateId <= 0) {
            throw new IllegalArgumentException();
        }

        if (!couponTemplateRepository.existsById(couponTemplateId)) {
            throw new CouponTemplateNotFoundException("쿠폰 템플릿이 존재하지 않습니다.");
        }

        if (!couponTemplateRepository.checkActiveCouponTemplate(couponTemplateId)) {
            throw new CouponTemplateNotFoundException("삭제된 쿠폰 템플릿입니다.");
        }

        couponTemplateRepository.updateCouponTemplate(couponTemplateId, couponTemplateUpdateRequestDto);
    }

    /**
     * 쿠폰 템플릿 삭제
     * @param couponTemplateId
     */
    @Override
    @Transactional
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

    /**
     * 쿠폰 템플릿 조회 - Pageable
     * @param pageable
     * @return List<CouponTemplateResponseDto>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CouponTemplateResponseDto> getCouponTemplates(Pageable pageable) {
        return couponTemplateRepository.findCouponTemplatesByPageable(pageable);
    }

    /**
     * 사용되지않은 쿠폰 템플릿 조회 - Pageable
     * @param pageable
     * @return Page<CouponTemplateResponseDto>
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CouponTemplateResponseDto> getCouponTemplatesUnused(Pageable pageable) {
        return couponTemplateRepository.findUnusedCouponTemplates(pageable);
    }
}
