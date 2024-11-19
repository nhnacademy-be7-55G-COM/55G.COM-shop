package shop.s5g.shop.dto.cart.request;

import jakarta.validation.constraints.NotNull;

public record CartBookSelectRequestDto (
    @NotNull
    Long bookId,

    boolean status
){

}
