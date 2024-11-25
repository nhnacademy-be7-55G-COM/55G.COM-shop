package shop.s5g.shop.dto.customer;

import shop.s5g.shop.entity.member.Customer;

public record CustomerResponseDto(
    Long customerId,
    String password,
    String name,
    String phoneNumber,
    String email
) {

    public static CustomerResponseDto of(Customer customer) {
        return new CustomerResponseDto(
            customer.getCustomerId(), customer.getPassword(), customer.getName(),
            customer.getPhoneNumber(), customer.getEmail()
        );
    }
}
