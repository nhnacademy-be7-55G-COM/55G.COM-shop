package shop.S5G.shop.service.coupon.template.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.S5G.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.S5G.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.S5G.shop.entity.coupon.CouponTemplate;
import shop.S5G.shop.repository.coupon.template.CouponTemplateRepository;
import shop.S5G.shop.service.coupon.template.CouponTemplateService;

@Service
@RequiredArgsConstructor
public class CouponTemplateServiceImpl implements CouponTemplateService {

    private final CouponTemplateRepository couponTemplateRepository;

    @Override
    public void createCouponTemplate(CouponTemplateRequestDto couponTemplateRequestDto) {

    }

    @Override
    public CouponTemplateResponseDto findCouponTemplate(Long couponTemplateId) {
        return null;
    }

    @Override
    public void updateCouponTemplate(CouponTemplateRequestDto couponTemplateRequestDto) {

    }

    @Override
    public void deleteCouponTemplate(Long couponTemplateId) {

    }
}
