package shop.S5G.shop.dto.cart.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CartDeleteBookRequestDto(

    @NotNull
    Long bookId
) {

}
