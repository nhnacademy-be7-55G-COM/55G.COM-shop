package shop.s5g.shop.repository.coupon.category.qdsl;

public interface CouponCategoryQuerydslRepository {

    // delete - CouponTemplate 구현 예정

    // check
    boolean existsByCouponTemplateAndCategory(Long couponTemplateId, Long categoryId);
}
