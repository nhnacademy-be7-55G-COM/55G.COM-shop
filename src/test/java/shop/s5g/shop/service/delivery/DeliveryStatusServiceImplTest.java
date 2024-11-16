package shop.s5g.shop.service.delivery;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.repository.delivery.DeliveryStatusRepository;
import shop.s5g.shop.service.delivery.impl.DeliveryStatusServiceImpl;

@ExtendWith(MockitoExtension.class)
class DeliveryStatusServiceImplTest {
    @Mock
    DeliveryStatusRepository deliveryStatusRepository;

    @InjectMocks
    DeliveryStatusServiceImpl service;

    @Test
    void getAllStatusTest() {
        assertThatCode(() -> service.getAllStatus()).doesNotThrowAnyException();
    }

}
