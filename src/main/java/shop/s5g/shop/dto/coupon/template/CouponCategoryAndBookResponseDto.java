package shop.s5g.shop.dto.coupon.template;

import jakarta.annotation.Nullable;

public record CouponCategoryAndBookResponseDto(

    @Nullable
    Long categoryId,

    @Nullable
    Long bookId,

    @Nullable
    String categoryName,

    @Nullable
    String bookName
) {

}
