package shop.S5G.shop.dto.publisher;

import jakarta.validation.constraints.NotNull;

public record PublisherResponseDto (

    @NotNull
    String name,

    @NotNull
    boolean active
){

}
