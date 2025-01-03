package shop.s5g.shop.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CustomerRegistrationRequestDto(
    @NotNull
    @Length(max = 255)
    String password,

    @NotNull
    @Length(min = 1, max = 30)
    String name,

    @NotNull
    @Length(min = 11, max = 11)
    String phoneNumber,

    @NotNull
    @Length(min = 1, max = 300)
    @Email
    String email
) {

}
