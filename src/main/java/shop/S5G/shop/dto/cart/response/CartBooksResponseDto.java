package shop.S5G.shop.dto.cart.response;


import java.math.BigDecimal;

public record CartBooksResponseDto(
    Long price,
    BigDecimal discountedPrice,
    Integer quantity,
    Integer stock
) {

}
