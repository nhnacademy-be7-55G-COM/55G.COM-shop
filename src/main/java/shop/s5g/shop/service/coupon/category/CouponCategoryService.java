package shop.s5g.shop.service.coupon.category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.coupon.category.CouponCategoryDetailsForCategoryDto;
import shop.s5g.shop.dto.coupon.category.CouponCategoryRequestDto;
import shop.s5g.shop.dto.coupon.category.CouponCategoryResponseDto;

public interface CouponCategoryService {

    // create
    void createCouponCategory(CouponCategoryRequestDto couponCategoryRequestDto);

    // read
    Page<CouponCategoryResponseDto> getAllCouponCategories(Pageable pageable);

    Page<CouponCategoryDetailsForCategoryDto> getCategoriesByCouponTemplateId(Long couponTemplateId, Pageable pageable);
}
