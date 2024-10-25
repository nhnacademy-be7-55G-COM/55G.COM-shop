package shop.S5G.shop.repository.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.entity.member.Customer;

@DataJpaTest
@Transactional
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = Customer.builder()
            .email("test@test.com")
            .name("Test User")
            .phoneNumber("010-1234-5678")
            .password("securePassword")
            .active(true)
            .build();

        testCustomer = customerRepository.save(testCustomer);
    }

    @Test
    void saveAndFindCustomerTest() {
        // When
        Optional<Customer> foundCustomer = customerRepository.findById(
            testCustomer.getCustomerId());

        // Then
        assertThat(foundCustomer).isPresent();
        assertThat(foundCustomer.get().getEmail()).isEqualTo("test@test.com");
        assertThat(foundCustomer.get().getName()).isEqualTo("Test User");
    }

    @Test
    void deleteCustomerTest() {
        // Given
        Long customerId = testCustomer.getCustomerId();

        // When
        customerRepository.delete(testCustomer);

        // Then
        Optional<Customer> deletedCustomer = customerRepository.findById(customerId);
        assertThat(deletedCustomer).isNotPresent();
    }

    @Test
    void existsByIdTest() {
        // When
        boolean exists = customerRepository.existsById(testCustomer.getCustomerId());

        // Then
        assertThat(exists).isTrue();
    }

//    @Test
//    void inactiveCustomerTest() {
//        // Given
//        Long customerId = testCustomer.getCustomerId();
//
//        // When
//        customerRepository.inactiveCustomer(customerId);
//        customerRepository.flush();
//
//        // Then
//        Optional<Customer> inactiveCustomer = customerRepository.findById(customerId);
//        assertThat(inactiveCustomer).isPresent();
//        assertThat(inactiveCustomer.get().isActive()).isFalse();
//    }
}
