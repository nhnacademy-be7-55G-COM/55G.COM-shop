package shop.S5G.shop.service.member.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.customer.CustomerRegistrationRequestDto;
import shop.S5G.shop.dto.customer.CustomerResponseDto;
import shop.S5G.shop.dto.customer.CustomerUpdateRequestDto;
import shop.S5G.shop.entity.member.Customer;
import shop.S5G.shop.exception.member.CustomerNotFoundException;
import shop.S5G.shop.repository.member.CustomerRepository;
import shop.S5G.shop.service.member.CustomerService;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer addCustomer(CustomerRegistrationRequestDto customerRegistrationRequestDto) {
        return customerRepository.save(new Customer(
            customerRegistrationRequestDto.password(),
            customerRegistrationRequestDto.name(),
            customerRegistrationRequestDto.phoneNumber(),
            customerRegistrationRequestDto.email()
        ));

    }

    @Override
    public void updateCustomer(long customerId, CustomerUpdateRequestDto customerUpdateRequestDto) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("변경할 고객이 존재하지 않습니다.");
        }
        customerRepository.updateCustomer(customerId, customerUpdateRequestDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDto getCustomer(long customerId) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException("고객이 존재하지 않습니다."));

        return new CustomerResponseDto(customer.getCustomerId(), customer.getPassword(),
            customer.getName(), customer.getPhoneNumber(), customer.getEmail());
    }

    @Override
    public void deleteCustomer(long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("고객이 존재하지 않습니다.");
        }
        customerRepository.inactiveCustomer(customerId);
    }
}
