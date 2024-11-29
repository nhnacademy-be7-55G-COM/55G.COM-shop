package shop.s5g.shop.repository.coupon.template.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateUpdateRequestDto;
import shop.s5g.shop.entity.coupon.CouponTemplate;

public interface CouponTemplateQuerydslRepository {

    CouponTemplateResponseDto findCouponTemplateById(Long couponTemplateId);

    void updateCouponTemplate(Long couponTemplateId, CouponTemplateUpdateRequestDto couponTemplateUpdateRequestDto);

    boolean checkActiveCouponTemplate(Long couponTemplateId);

    boolean existsCouponTemplateName(String type);

    void deleteCouponTemplate(Long couponTemplateId);

    Page<CouponTemplateResponseDto> findCouponTemplatesByPageable(Pageable pageable);

    Page<CouponTemplateResponseDto> findUnusedCouponTemplates(Pageable pageable);

    Page<CouponTemplateResponseDto> findCouponTemplatesExcludingBirthAndWelcome(Pageable pageable);

    CouponTemplate findParticularCouponByName(String keyword);

    Integer findCouponPolicyDurationByCouponTemplateId(Long couponTemplateId);
}
