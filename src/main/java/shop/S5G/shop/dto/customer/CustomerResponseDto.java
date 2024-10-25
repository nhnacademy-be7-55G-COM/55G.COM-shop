package shop.S5G.shop.dto.customer;

public record CustomerResponseDto(
    Long customerId,
    String password,
    String name,
    String phoneNumber,
    String email
) {

}
