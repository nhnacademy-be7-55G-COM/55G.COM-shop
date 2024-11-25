package shop.s5g.shop.dto.coupon.book;

import java.math.BigDecimal;

public record CouponBookResponseDto(

    Long couponTemplateId,

    Long bookId,

    String title,

    BigDecimal discountPrice,

    Long condition,

    Long maxPrice,

    Integer duration,

    String couponName,

    String couponDescription

) {

}
