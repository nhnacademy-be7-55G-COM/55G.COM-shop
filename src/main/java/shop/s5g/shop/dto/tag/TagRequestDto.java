package shop.s5g.shop.dto.tag;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TagRequestDto (
//    private Long publisherId;
    @Size(min = 1, max = 50)
    @NotNull
    String tagName,
    @NotNull
    boolean active
){
}
