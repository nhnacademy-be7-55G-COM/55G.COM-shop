package shop.s5g.shop.dto.point;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PointPolicyRemoveRequestDto (
    @NotNull
    String sourceName,

    @Min(0)
    long id
) {

}
