package shop.s5g.shop.repository.delivery;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import shop.s5g.shop.config.TestQueryFactoryConfig;
import shop.s5g.shop.dto.delivery.DeliveryFeeUpdateRequestDto;

@DataJpaTest
@Import(TestQueryFactoryConfig.class)
class DeliveryFeeRepositoryTest {
    @Autowired
    DeliveryFeeRepository repository;

    DeliveryFeeUpdateRequestDto updateRequest = new DeliveryFeeUpdateRequestDto(
        1L, 3000L, 2000L,10000, "테스트2"
    );
    DeliveryFeeUpdateRequestDto updateFailRequest = new DeliveryFeeUpdateRequestDto(
        2L, 3000L, 2000L,10000, "테스트2"
    );

    @Test
    @Sql("classpath:delivery-fee-test.sql")
    void updateFeeSuccessTest() {
        assertThat(repository.updateFee(updateRequest))
            .isPresent().get()
            .hasFieldOrPropertyWithValue("id", 1L)
            .matches(
                fee ->
                    fee.getFee() == updateRequest.fee() &&
                    fee.getCondition() == updateRequest.condition() &&
                    fee.getRefundFee() == updateRequest.refundFee() &&
                    fee.getName().equals(updateRequest.name())
            );
    }

    @Test
    @Sql("classpath:delivery-fee-test.sql")
    void updateFeeFailTest() {
        assertThat(repository.updateFee(updateFailRequest)).isEmpty();
    }
}
