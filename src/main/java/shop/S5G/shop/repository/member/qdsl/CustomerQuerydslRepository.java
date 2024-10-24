package shop.S5G.shop.repository.member.qdsl;

import shop.S5G.shop.dto.customer.CustomerUpdateRequestDto;

public interface CustomerQuerydslRepository {

    void updateCustomer(long customerId, CustomerUpdateRequestDto customerUpdateRequestDto);
}
