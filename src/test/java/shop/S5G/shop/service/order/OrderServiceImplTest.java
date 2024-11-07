package shop.S5G.shop.service.order;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import shop.S5G.shop.repository.order.OrderRepository;
import shop.S5G.shop.service.order.impl.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderServiceImpl orderService;

    @Test
    void getAllOrdersWithDetailTest() {
//        when(orderRepository.findOrdersByCustomerId(anyLong(), any(), )).thenReturn(Page.empty());
        Pageable pageable = mock(Pageable.class);
        assertThatCode(() -> orderService.getAllOrdersWithDetail(1L))
            .doesNotThrowAnyException();
//        verify(orderRepository, times(1)).findOrdersByCustomerId(eq(1L), eq(pageable), );
    }
}
