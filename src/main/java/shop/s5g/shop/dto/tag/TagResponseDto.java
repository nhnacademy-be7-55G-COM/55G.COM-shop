package shop.s5g.shop.dto.tag;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TagResponseDto (
    @NotNull
    Long tagId,
    @NotNull
    String tagName,
    boolean active
) {
}
