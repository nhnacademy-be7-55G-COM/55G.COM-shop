package shop.s5g.shop.dto.member;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record MemberRegistrationRequestDto(
    @NotBlank
    @Length(min = 1, max = 30)
    String name,

    @NotBlank
    @Length(min = 8, max = 300)
    @Email
    String email,

    @NotBlank
    @Length(min = 4, max = 30)
    String loginId,

    @NotBlank
    @Length(min = 4, max = 30)
    String password,

    @NotBlank
    @Length(min = 11, max = 11)
    String phoneNumber,

    @NotBlank
    @Length(min = 8, max = 8)
    String birthDate
) {

}
