package shop.s5g.shop.repository.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import shop.s5g.shop.config.TestQueryFactoryConfig;
import shop.s5g.shop.dto.order.OrderDetailWithBookResponseDto;
import shop.s5g.shop.entity.order.OrderDetail;

@DataJpaTest
@ActiveProfiles({"embed-db", "test"})
@Import(TestQueryFactoryConfig.class)
class OrderDetailRepositoryTest {
    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Test
    @Sql("classpath:order-test.sql")
    void countOrderDetailsByOrderIdTest() {
        assertThat(orderDetailRepository.countOrderDetailsByOrderId(1L))
            .isEqualTo(3L);
        assertThat(orderDetailRepository.countOrderDetailsByOrderId(2L))
            .isEqualTo(2L);
        assertThat(orderDetailRepository.countOrderDetailsByOrderId(Long.MAX_VALUE))
            .isZero();
    }

    @Test
    @Sql("classpath:order-test.sql")
    void queryAllDetailsByOrderIdTest() {
        List<OrderDetailWithBookResponseDto> result = orderDetailRepository.queryAllDetailsByOrderId(1L);

        assertThat(result).hasSize(3);
        assertThat(result.get(0))
            .hasFieldOrPropertyWithValue("bookTitle", "테스트 타이틀1")
            .hasFieldOrPropertyWithValue("quantity", 3);
        assertThat(result.get(1))
            .hasFieldOrPropertyWithValue("bookTitle", "테스트 타이틀2")
            .hasFieldOrPropertyWithValue("quantity", 1);
        assertThat(result.get(2))
            .hasFieldOrPropertyWithValue("bookTitle", "테스트 타이틀3")
            .hasFieldOrPropertyWithValue("quantity", 1);
    }

    @Test
    @Sql("classpath:order-test.sql")
    void fetchOrderDetailsByOrderIdTest() {
        final long orderId = 1L;
        List<OrderDetail> result = orderDetailRepository.fetchOrderDetailsByOrderId(orderId);

        assertThat(result).hasSize(3);
        assertThat(result.getFirst())
            .hasFieldOrPropertyWithValue("quantity", 3);

        // fetch join을 하는지 로그를 볼 것.
        assertThat(result.getFirst().getBook())
            .hasFieldOrPropertyWithValue("title", "테스트 타이틀1");
        assertThat(result.getFirst().getOrder())
            .hasFieldOrPropertyWithValue("id", orderId);

    }
}
