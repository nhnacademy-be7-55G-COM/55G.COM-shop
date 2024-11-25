package shop.s5g.shop.service.member;

import shop.s5g.shop.dto.customer.CustomerRegistrationRequestDto;
import shop.s5g.shop.dto.customer.CustomerResponseDto;
import shop.s5g.shop.dto.customer.CustomerUpdateRequestDto;
import shop.s5g.shop.entity.member.Customer;

public interface CustomerService {

    Customer addCustomer(CustomerRegistrationRequestDto customerRegistrationRequestDto);

    void updateCustomer(long customerId, CustomerUpdateRequestDto customerUpdateRequestDto);

    CustomerResponseDto getCustomer(long customerId);

    void deleteCustomer(long customerId);

    CustomerResponseDto queryCustomer(String phoneNumber, String name, String password);

    CustomerResponseDto getOrRegisterCustomerPhoneNumber(String phoneNumber, String name, String password);
}
