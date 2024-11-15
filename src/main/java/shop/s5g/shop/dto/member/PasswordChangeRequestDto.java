package shop.s5g.shop.dto.member;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record PasswordChangeRequestDto(
    @NotBlank
    @Length(min = 4)
    String oldPassword,

    @NotBlank
    @Length(min = 4, max = 30)
    String newPassword
) {

}
