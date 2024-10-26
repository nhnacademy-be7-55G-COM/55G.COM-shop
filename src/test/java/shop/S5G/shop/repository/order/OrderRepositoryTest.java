package shop.S5G.shop.repository.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

// TODO: 다른 Entity들이 업데이트 되었을때 하기
//@Disabled
@DataJpaTest
@ActiveProfiles("local")
class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;

    @Test
    @Sql("classpath:order-test.sql")
    void querydslTest() {
        orderRepository.findOrdersByCustomerId(1L);
    }
}
