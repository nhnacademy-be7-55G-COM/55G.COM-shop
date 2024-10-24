package shop.S5G.shop.dto.member;


public record MemberRegistrationRequestDto(
    String name,

    String email,

    String loginId,

    String password,

    String phoneNumber,

    String birthDate
) {

}
