package shop.s5g.shop.service.member.impl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.customer.CustomerRegistrationRequestDto;
import shop.s5g.shop.dto.customer.CustomerResponseDto;
import shop.s5g.shop.dto.customer.CustomerUpdateRequestDto;
import shop.s5g.shop.entity.member.Customer;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.exception.member.CustomerNotFoundException;
import shop.s5g.shop.repository.member.CustomerRepository;
import shop.s5g.shop.service.member.CustomerService;

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

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDto queryCustomer(String phoneNumber, String name, String password) {
        return customerRepository.findByPhoneNumberAndNameAndPassword(
            phoneNumber, name, customerPasswordHash(password, name)
        ).map(CustomerResponseDto::of).orElseThrow(
            () -> new CustomerNotFoundException("휴대폰 번호와 비밀번호가 맞는 고객이 존재하지 않습니다.")
        );
    }

    @Override
    public CustomerResponseDto getOrRegisterCustomerPhoneNumber(String phoneNumber, String name,
        String password) {
        String encodedPw = customerPasswordHash(password, name);
        if (phoneNumber == null) {
            throw new BadRequestException("휴대폰번호는 null 이 될 수 없습니다.");
        }
        Optional<Customer> c = customerRepository.getCustomerByPhoneNumber(phoneNumber, name, encodedPw);
        return c.map(CustomerResponseDto::of).orElseGet(
            () -> {
                // email 필드가 NULL 이 아니라서 일단 해시값으로 채움.
                String emailAlter = DigestUtils.md5Hex(password + name + phoneNumber);
                emailAlter = emailAlter + '@' + emailAlter;
                Customer newCustomer = new Customer(encodedPw, name, phoneNumber, emailAlter);
                return CustomerResponseDto.of(customerRepository.save(newCustomer));
            }
        );
    }

    private String customerPasswordHash(String password, String name) {
        return DigestUtils.sha256Hex(name + password + name);
    }

    @Override
    public Customer addCustomerByMember(Customer customer) {
        return customerRepository.save(customer);
    }
}
