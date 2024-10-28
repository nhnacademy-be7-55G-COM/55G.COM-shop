package shop.S5G.shop.repository.order;

import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

// TODO: CRUD 테스트 추가할것
@Disabled
@DataJpaTest
public class OrderDetailRepositoryTest {
    @Autowired
    OrderDetailRepository orderDetailRepository;

}
