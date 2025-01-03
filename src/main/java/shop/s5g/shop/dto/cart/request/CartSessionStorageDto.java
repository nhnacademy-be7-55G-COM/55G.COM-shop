package shop.s5g.shop.dto.cart.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;



public record CartSessionStorageDto(
    @NotNull
    @Valid
    List<CartBookInfoRequestDto> cartBookInfoList
){}
