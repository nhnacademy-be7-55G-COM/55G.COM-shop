package shop.s5g.shop.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record AddressRequestDto(
    @NotBlank
    @Length(max = 50)
    String primaryAddress,

    @Length(max = 255)
    String detailAddress,

    @Length(max = 255)
    String alias,

    @NotNull
    boolean isDefault

) {

}
