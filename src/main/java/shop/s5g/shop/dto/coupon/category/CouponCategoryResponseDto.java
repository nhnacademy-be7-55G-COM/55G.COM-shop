package shop.s5g.shop.dto.coupon.category;

import java.math.BigDecimal;

public record CouponCategoryResponseDto(

    String categoryName,

    BigDecimal discountPrice,

    Long condition,

    Long maxPrice,

    Integer duration,

    String couponName,

    String couponDescription
) {

}