package shop.S5G.shop.dto.customer;

public record CustomerRegistrationRequestDto(
    String password,

    String name,

    String phoneNumber,

    String email
) {

}
