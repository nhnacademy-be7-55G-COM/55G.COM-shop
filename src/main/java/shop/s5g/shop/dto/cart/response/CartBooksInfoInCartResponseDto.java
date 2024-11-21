package shop.s5g.shop.dto.cart.response;

import java.math.BigDecimal;

public record CartBooksInfoInCartResponseDto(
    Long bookId,
    long price,
    BigDecimal discountRate,
    int stock,
    String title,
    String imageName
) {

}
