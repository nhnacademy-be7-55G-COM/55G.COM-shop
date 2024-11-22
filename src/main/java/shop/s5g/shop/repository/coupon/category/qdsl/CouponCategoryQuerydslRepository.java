package shop.s5g.shop.repository.coupon.category.qdsl;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.coupon.category.CouponCategoryDetailsForCategoryDto;
import shop.s5g.shop.dto.coupon.category.CouponCategoryResponseDto;

public interface CouponCategoryQuerydslRepository {

    // delete - CouponTemplate 구현 예정

    // check
    boolean existsByCouponTemplateAndCategory(Long couponTemplateId, Long categoryId);

    // read
    Page<CouponCategoryResponseDto> findAllCouponCategories(Pageable pageable);
    Page<CouponCategoryDetailsForCategoryDto> findCategoryInfoByCouponTemplate(Pageable pageable);
}
