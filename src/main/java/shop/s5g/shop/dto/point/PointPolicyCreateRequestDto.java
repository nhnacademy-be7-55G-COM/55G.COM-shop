package shop.s5g.shop.dto.point;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record PointPolicyCreateRequestDto(
    @NotBlank
    String policyName,

    @NotBlank
    String discountType,

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "값은 0 이상이어야 합니다.")
    @Digits(integer = 10, fraction = 2, message = "최대 소수점 둘째 자리까지만 허용됩니다.")
    BigDecimal discountValue,

    @Min(1)
    long pointSourceId
){

}
