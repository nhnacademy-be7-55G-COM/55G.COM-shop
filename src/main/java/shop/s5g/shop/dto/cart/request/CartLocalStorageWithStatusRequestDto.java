package shop.s5g.shop.dto.cart.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CartLocalStorageWithStatusRequestDto(

    @NotNull
    @Valid
    List<CartBookInfoWithStatusRequestDto> cartBookInfoList
) {

}
