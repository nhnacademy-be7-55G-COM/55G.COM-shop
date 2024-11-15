package shop.s5g.shop.dto.cart.response;

import java.math.BigDecimal;

public record CartBooksResponseDto(
    Long bookId,
    Long price,
    BigDecimal discountedPrice,
    Integer quantity,
    Integer stock,
    String title

) {}
