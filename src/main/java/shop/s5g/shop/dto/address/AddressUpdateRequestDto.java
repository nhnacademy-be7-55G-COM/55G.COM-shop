package shop.s5g.shop.dto.address;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record AddressUpdateRequestDto(
    @Length(max = 255)
    String alias,

    @NotNull
    boolean isDefault
) {

}
