package shop.S5G.shop.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.S5G.shop.entity.member.Customer;
import shop.S5G.shop.repository.member.qdsl.CustomerQuerydslRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long>,
    CustomerQuerydslRepository {

}