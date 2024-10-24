package shop.S5G.shop.repository.member.qdsl.impl;

import static shop.S5G.shop.entity.member.QCustomer.customer;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.S5G.shop.dto.customer.CustomerUpdateRequestDto;
import shop.S5G.shop.entity.member.Customer;
import shop.S5G.shop.repository.member.qdsl.CustomerQuerydslRepository;

public class CustomerQuerydslRepositoryImpl extends QuerydslRepositorySupport implements
    CustomerQuerydslRepository {

    public CustomerQuerydslRepositoryImpl() {
        super(Customer.class);
    }

    @Override
    public void updateCustomer(long customerId, CustomerUpdateRequestDto customerUpdateRequestDto) {
        update(customer)
            .set(customer.email, customerUpdateRequestDto.email())
            .set(customer.name, customerUpdateRequestDto.name())
            .set(customer.phoneNumber, customerUpdateRequestDto.phoneNumber())
            .where(customer.customerId.eq(customerId))
            .execute();
    }
}
