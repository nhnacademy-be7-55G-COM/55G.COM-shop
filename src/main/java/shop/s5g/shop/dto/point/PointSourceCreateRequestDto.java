package shop.s5g.shop.dto.point;

import jakarta.validation.constraints.NotBlank;

public record PointSourceCreateRequestDto(
    @NotBlank
    String pointSourceName
) {


}
