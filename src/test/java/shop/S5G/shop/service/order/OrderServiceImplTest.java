package shop.S5G.shop.service.order;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
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
    void queryAllOrdersByCustomerIdTest() {
        when(orderRepository.findOrdersByCustomerId(anyLong(), any())).thenReturn(Page.empty());
        Pageable pageable = mock(Pageable.class);
        assertThatCode(() -> orderService.queryAllOrdersByCustomerId(1L, pageable))
            .doesNotThrowAnyException();
        verify(orderRepository, times(1)).findOrdersByCustomerId(eq(1L), eq(pageable));
    }
}
