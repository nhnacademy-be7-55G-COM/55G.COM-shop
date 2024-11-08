package shop.S5G.shop.dto.cart.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record CartControlQuantityRequestDto(

    @NotNull
    Long bookId,

    @Range(min = -1, max = 1)
    int change
) {

}
