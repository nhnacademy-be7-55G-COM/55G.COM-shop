package shop.s5g.shop.dto.cart.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CartDeleteBookRequestDto(
    @NotBlank
    String sessionId,
    @NotNull
    Long bookId
) {

}
