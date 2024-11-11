package shop.s5g.shop.dto.admin;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record AdminRegistrationRequestDto(
    @NotBlank
    @Length(min = 4, max = 30)
    String id,

    @NotBlank
    @Length(min = 4, max = 30)
    String password
) {

}
