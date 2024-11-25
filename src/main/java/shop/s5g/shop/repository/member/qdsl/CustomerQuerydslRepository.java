package shop.s5g.shop.repository.member.qdsl;

import java.util.Optional;
import shop.s5g.shop.dto.customer.CustomerUpdateRequestDto;
import shop.s5g.shop.entity.member.Customer;

public interface CustomerQuerydslRepository {

    void updateCustomer(long customerId, CustomerUpdateRequestDto customerUpdateRequestDto);

    void inactiveCustomer(long customerId);

    Optional<Customer> getCustomerByPhoneNumber(
        String phoneNumber, String name, String password
    );
}
