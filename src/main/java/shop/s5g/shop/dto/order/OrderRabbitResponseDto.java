package shop.s5g.shop.dto.order;

public record OrderRabbitResponseDto(
    boolean wellOrdered,
    String message
) {

}
