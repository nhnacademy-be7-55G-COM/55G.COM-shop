package shop.s5g.shop.repository.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.member.Customer;
import shop.s5g.shop.repository.member.qdsl.CustomerQuerydslRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long>,
    CustomerQuerydslRepository {

    Optional<Customer> findByPhoneNumberAndNameAndPassword(String phoneNumber, String name, String password);
}
