package shop.s5g.shop.repository.coupon.template.qdsl;

import java.util.List;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.s5g.shop.entity.coupon.CouponPolicy;

public interface CouponTemplateQuerydslRepository {

    CouponTemplateResponseDto findCouponTemplateById(Long couponTemplateId);

    void updateCouponTemplate(Long couponTemplateId, CouponPolicy couponPolicy, CouponTemplateRequestDto couponTemplateRequestDto);

    boolean checkActiveCouponTemplate(Long couponTemplateId);

    void deleteCouponTemplate(Long couponTemplateId);

    List<CouponTemplateResponseDto> findCouponTemplatesByPageable(Pageable pageable);
}
