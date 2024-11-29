package shop.s5g.shop.service.coupon.coupon.impl;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.config.RedisConfig;
import shop.s5g.shop.dto.coupon.coupon.CouponRequestDto;
import shop.s5g.shop.entity.coupon.Coupon;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.exception.coupon.CouponTemplateNotFoundException;
import shop.s5g.shop.repository.coupon.coupon.CouponRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;
import shop.s5g.shop.service.coupon.coupon.RedisCouponService;
import shop.s5g.shop.util.CouponUtil;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@ConditionalOnBean(RedisConfig.class)
public class RedisCouponServiceImpl implements RedisCouponService {

    private final CouponStorageService couponStorageService;
    private final CouponTemplateRepository couponTemplateRepository;
    private final CouponRepository couponRepository;

    /**
     * 쿠폰 생성 ( 1 ~ n개 생성 가능 )
     * @param couponRequestDto
     */
    @Override
    public void createCoupon(CouponRequestDto couponRequestDto) {

        if (Objects.isNull(couponRequestDto.quantity()) || couponRequestDto.quantity() <= 0) {
            throw new IllegalArgumentException("쿠폰 수량이 잘못 지정되었습니다.");
        }

        CouponTemplate couponTemplate = couponTemplateRepository.findById(couponRequestDto.couponTemplateId())
            .orElseThrow(() -> new CouponTemplateNotFoundException("쿠폰 템플릿을 찾을 수 없습니다."));

        Set<String> uniqueCode = new HashSet<>();
        Set<Coupon> couponSet = new HashSet<>();

        while (uniqueCode.size() < couponRequestDto.quantity()) {
            String couponCode = CouponUtil.createUniqueCouponNumber();

            if (uniqueCode.contains(couponCode)) {
                continue;
            }
            uniqueCode.add(couponCode);
        }

        int saveCouponCnt = 0;
        for (String code : uniqueCode) {

            Coupon coupon = new Coupon(
                couponTemplate,
                code
            );
            // 한 번에 쿠폰 천장까지만 저장할 예정
            if (saveCouponCnt < 1000) {
                couponStorageService.addIssuedCoupon(couponTemplate.getCouponTemplateId(), code);
            }
            couponSet.add(coupon);

            saveCouponCnt++;
        }

        couponStorageService.setCouponCnt(couponTemplate.getCouponTemplateId(), couponRequestDto.quantity());

        couponRepository.saveAll(couponSet);

    }
}
