package shop.s5g.shop.dto.cart.request;

import jakarta.validation.constraints.NotNull;

public record CartDeleteBookRequestDto(

    @NotNull
    Long bookId
) {

}
