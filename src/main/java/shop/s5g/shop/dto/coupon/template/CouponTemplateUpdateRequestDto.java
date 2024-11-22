package shop.s5g.shop.dto.coupon.template;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CouponTemplateUpdateRequestDto(
    
    @NotNull
    Long couponTemplateId,
    
    @NotBlank(message = "쿠폰 이름은 비어있어서는 안됩니다.")
    String couponName,
    
    @NotBlank(message = "쿠폰 설명이 비어있어서는 안됩니다.")
    String couponDescription
) {

}
