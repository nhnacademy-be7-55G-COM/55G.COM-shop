package shop.S5G.shop.dto.cart.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


public record CartPutRequestDto(


    @NotNull
    Long bookId,

    @NotNull
    @Min(1)
    Integer quantity

){}
