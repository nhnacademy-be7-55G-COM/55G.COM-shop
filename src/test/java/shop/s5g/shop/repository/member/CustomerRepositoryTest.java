package shop.s5g.shop.repository.member;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.s5g.shop.config.TestQueryFactoryConfig;
import shop.s5g.shop.entity.member.Customer;

@DataJpaTest
@Import(TestQueryFactoryConfig.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer;

    @PersistenceContext
    private EntityManager em;

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

    @Test
    void inactiveCustomerTest() {
        // given
        testCustomer = customerRepository.save(testCustomer);
        long customerId = testCustomer.getCustomerId();

        // when
        customerRepository.inactiveCustomer(customerId);
        customerRepository.flush(); // 변경사항 DB에 반영
        em.clear();
        // then
        Customer inactiveCustomer = customerRepository.findById(customerId).orElseThrow();
        assertThat(inactiveCustomer.isActive()).isFalse();
    }
}
