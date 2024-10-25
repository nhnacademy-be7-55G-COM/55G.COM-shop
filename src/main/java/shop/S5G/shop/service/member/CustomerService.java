package shop.S5G.shop.service.member;

import shop.S5G.shop.dto.customer.CustomerRegistrationRequestDto;
import shop.S5G.shop.dto.customer.CustomerResponseDto;
import shop.S5G.shop.dto.customer.CustomerUpdateRequestDto;
import shop.S5G.shop.entity.member.Customer;

public interface CustomerService {

    Customer addCustomer(CustomerRegistrationRequestDto customerRegistrationRequestDto);

    void updateCustomer(long customerId, CustomerUpdateRequestDto customerUpdateRequestDto);

    CustomerResponseDto getCustomer(long customerId);

    void deleteCustomer(long customerId);
}
