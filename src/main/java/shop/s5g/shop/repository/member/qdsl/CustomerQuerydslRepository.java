package shop.s5g.shop.repository.member.qdsl;

import shop.s5g.shop.dto.customer.CustomerUpdateRequestDto;

public interface CustomerQuerydslRepository {

    void updateCustomer(long customerId, CustomerUpdateRequestDto customerUpdateRequestDto);

    void inactiveCustomer(long customerId);
}
