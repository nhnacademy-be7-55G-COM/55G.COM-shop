package shop.s5g.shop.service.coupon.category.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.coupon.category.CouponCategoryDetailsForCategoryDto;
import shop.s5g.shop.dto.coupon.category.CouponCategoryRequestDto;
import shop.s5g.shop.dto.coupon.category.CouponCategoryResponseDto;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.entity.coupon.CouponCategory;
import shop.s5g.shop.entity.coupon.CouponTemplate;
import shop.s5g.shop.exception.category.CategoryResourceNotFoundException;
import shop.s5g.shop.exception.coupon.CouponCategoryAlreadyExistsException;
import shop.s5g.shop.exception.coupon.CouponTemplateNotFoundException;
import shop.s5g.shop.repository.category.CategoryRepository;
import shop.s5g.shop.repository.coupon.category.CouponCategoryRepository;
import shop.s5g.shop.repository.coupon.template.CouponTemplateRepository;
import shop.s5g.shop.service.coupon.category.CouponCategoryService;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponCategoryServiceImpl implements CouponCategoryService {

    private final CouponCategoryRepository couponCategoryRepository;
    private final CouponTemplateRepository couponTemplateRepository;
    private final CategoryRepository categoryRepository;

    /**
     * 카테고리 쿠폰 생성
     * @param couponCategoryRequestDto
     */
    @Override
    public void createCouponCategory(CouponCategoryRequestDto couponCategoryRequestDto) {

        Category category = categoryRepository.findById(couponCategoryRequestDto.categoryId())
            .orElseThrow(() -> new CategoryResourceNotFoundException("해당 카테고리를 찾을 수 없습니다."));

        CouponTemplate couponTemplate = couponTemplateRepository.findById(
            couponCategoryRequestDto.couponTemplateId())
            .orElseThrow(() -> new CouponTemplateNotFoundException("해당 쿠폰 템플릿이 존재하지 않습니다."));

        if (couponCategoryRepository.existsByCouponTemplateAndCategory(
            couponCategoryRequestDto.couponTemplateId(),
            couponCategoryRequestDto.categoryId())) {
            throw new CouponCategoryAlreadyExistsException("이미 존재하는 카테고리 쿠폰입니다.");
        }

        couponCategoryRepository.save(
            new CouponCategory(
                couponTemplate,
                category
            ));
    }

    @Override
    public Page<CouponCategoryResponseDto> getAllCouponCategories(Pageable pageable) {
        return couponCategoryRepository.findAllCouponCategories(pageable);
    }

    /**
     * 쿠폰이 적용된 카테고리 전부 가져오기
     * @param pageable
     * @return
     */
    @Override
    public Page<CouponCategoryDetailsForCategoryDto> getCategoriesByCouponTemplate(Pageable pageable) {
        return couponCategoryRepository.findCategoryInfoByCouponTemplate(pageable);
    }
}
