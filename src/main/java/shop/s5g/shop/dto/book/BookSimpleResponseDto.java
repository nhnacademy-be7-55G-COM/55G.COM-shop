package shop.s5g.shop.dto.book;

import java.math.BigDecimal;

public record BookSimpleResponseDto(
    long id,
    String title,
    long price,
    BigDecimal discountRate,
    int stock,
    boolean isPacked,
    String status
) {

}
