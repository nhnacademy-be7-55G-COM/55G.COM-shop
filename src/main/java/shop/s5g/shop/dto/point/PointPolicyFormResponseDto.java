package shop.s5g.shop.dto.point;

import jakarta.validation.constraints.Min;

public record PointPolicyFormResponseDto(


    long pointSourceId,

    String pointSourceName

) {

}
