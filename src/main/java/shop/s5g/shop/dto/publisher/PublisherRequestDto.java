package shop.s5g.shop.dto.publisher;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PublisherRequestDto (
    @Size(min = 1, max = 50)
    @NotNull
    String name,

    @NotNull
    boolean active
){
}
