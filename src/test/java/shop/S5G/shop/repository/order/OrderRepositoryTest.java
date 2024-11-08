package shop.S5G.shop.repository.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import shop.S5G.shop.config.QueryFactoryConfig;
import shop.S5G.shop.dto.order.OrderWithDetailResponseDto;

@DataJpaTest
@ActiveProfiles({"test", "embed-db"})
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(QueryFactoryConfig.class)
class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;

    @Test
    @Sql("classpath:order-test.sql")
    void findOrderByCustomerIdTest() {
        List<OrderWithDetailResponseDto> result = orderRepository.findOrdersByCustomerId(1L);
        log.debug("result: {}", result.toString());

        assertThat(result).hasSize(2);
        // 최근순 정렬임!
        assertThat(result.get(0))
            .hasFieldOrPropertyWithValue("totalKind", 2L)
            .hasFieldOrPropertyWithValue("totalQuantity", 3)
            .hasFieldOrPropertyWithValue("orderId", 2L)
        ;
        assertThat(result.get(1))
            .hasFieldOrPropertyWithValue("totalKind", 3L)
            .hasFieldOrPropertyWithValue("totalQuantity", 5)
            .hasFieldOrPropertyWithValue("orderId", 1L)
        ;
    }

    @Test
    @Sql("classpath:order-test.sql")
    void findOrderByCustomerIdEmptyTest() {
        List<OrderWithDetailResponseDto> result = orderRepository.findOrdersByCustomerId(Long.MAX_VALUE);
        log.debug("result: {}", result.toString());

        assertThat(result).isEmpty();
    }
}
