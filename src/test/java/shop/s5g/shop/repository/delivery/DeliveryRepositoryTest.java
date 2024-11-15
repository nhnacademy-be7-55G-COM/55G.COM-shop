package shop.s5g.shop.repository.delivery;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import shop.s5g.shop.config.TestQueryFactoryConfig;

@DataJpaTest
@Import(TestQueryFactoryConfig.class)
class DeliveryRepositoryTest {
    @Autowired
    DeliveryRepository repository;

    @Test
    @Sql("classpath:order-test.sql")
    void findByIdFetchTest() {
        assertThat(repository.findByIdFetch(1L))
            .isPresent().get()
            .matches(
                delivery ->
                    delivery.getAddress().equals("테스트 주소1") &&
                    delivery.getReceiverName().equals("ㅁㄴㅇㄹ")
            );
    }
}
