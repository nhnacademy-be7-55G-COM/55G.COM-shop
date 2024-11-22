package shop.s5g.shop.dto.cart.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartBookInfoWithStatusRequestDto (

    @NotNull
    Long bookId,

    @Min(1)
    int quantity,


    boolean status


){

}
