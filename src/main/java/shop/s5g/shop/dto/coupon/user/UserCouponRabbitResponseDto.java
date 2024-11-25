package shop.s5g.shop.dto.coupon.user;

public record UserCouponRabbitResponseDto(
    boolean isMessageProcessed,
    String message
) {

}