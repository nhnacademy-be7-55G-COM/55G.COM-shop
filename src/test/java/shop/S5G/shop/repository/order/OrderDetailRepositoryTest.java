package shop.S5G.shop.repository.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import shop.S5G.shop.dto.order.OrderDetailWithBookResponseDto;

@DataJpaTest
@ActiveProfiles({"embed-db", "test"})
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
}
