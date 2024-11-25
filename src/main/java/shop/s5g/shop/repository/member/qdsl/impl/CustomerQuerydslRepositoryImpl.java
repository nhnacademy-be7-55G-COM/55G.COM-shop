package shop.s5g.shop.repository.member.qdsl.impl;


import static shop.s5g.shop.entity.member.QCustomer.customer;

import com.querydsl.core.BooleanBuilder;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.customer.CustomerUpdateRequestDto;
import shop.s5g.shop.entity.member.Customer;
import shop.s5g.shop.repository.member.qdsl.CustomerQuerydslRepository;

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

    @Override
    public void inactiveCustomer(long customerId) {
        update(customer)
            .set(customer.active, false)
            .where(customer.customerId.eq(customerId))
            .execute();
    }

    @Override
    public Optional<Customer> getCustomerByPhoneNumber(
        String phoneNumber, String name, String password
    ) {
        BooleanBuilder builder = new BooleanBuilder();
        builder
            .and(customer.phoneNumber.eq(phoneNumber))
            .and(customer.name.eq(name))
            .and(customer.password.eq(password));

        return Optional.ofNullable(
            from(customer)
                .where(builder)
                .fetchOne()
        );
    }
}
