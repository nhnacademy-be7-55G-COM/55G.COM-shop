package shop.S5G.shop.dto.member;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record MemberRegistrationRequestDto(
    @NotNull
    @Length(min = 1, max = 30)
    String name,

    @NotNull
    @Length(min = 4, max = 300)
    @Email
    String email,

    @NotNull
    @Length(min = 4, max = 30)
    String loginId,

    @NotNull
    @Length(min = 4, max = 30)
    String password,

    @NotNull
    @Length(min = 11, max = 11)
    String phoneNumber,

    @NotNull
    @Length(min = 8, max = 8)
    String birthDate
) {

}
