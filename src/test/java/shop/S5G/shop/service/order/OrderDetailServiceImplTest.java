package shop.S5G.shop.service.order;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.S5G.shop.dto.order.OrderDetailWithBookResponseDto;
import shop.S5G.shop.exception.order.OrderDetailsNotExistException;
import shop.S5G.shop.repository.order.OrderDetailRepository;
import shop.S5G.shop.service.order.impl.OrderDetailServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrderDetailServiceImplTest {
    @Mock
    OrderDetailRepository orderDetailRepository;

    @InjectMocks
    OrderDetailServiceImpl service;

    @Test
    @DisplayName("유효하지 않은 Order ID 에러 테스트")
    void findOrderDetailByOrderIdNotFoundTest() {
        // countOrderDetailsByOrderId를 사용하지 않는 코드가 된다면 바뀔 여지가 있음.
        Mockito.when(orderDetailRepository.countOrderDetailsByOrderId(anyLong())).thenReturn(0L);

        assertThatThrownBy(() -> service.getOrderDetailsWithBook(1L))
            .isInstanceOf(OrderDetailsNotExistException.class)
            .hasMessageContaining("1");

        Mockito.verify(orderDetailRepository, never()).queryAllDetailsByOrderId(anyLong());
        Mockito.verify(orderDetailRepository, times(1)).countOrderDetailsByOrderId(eq(1L));
    }

    @Test
    @DisplayName("주문 상세 가져오기")
    void findOrderDetailByOrderIdTest() {
        OrderDetailWithBookResponseDto mockDto = mock(OrderDetailWithBookResponseDto.class);
        List<OrderDetailWithBookResponseDto> list = List.of(mockDto);
        Mockito.when(orderDetailRepository.countOrderDetailsByOrderId(anyLong())).thenReturn(1L);
        Mockito.when(orderDetailRepository.queryAllDetailsByOrderId(anyLong())).thenReturn(list);

        assertThatCode(() -> service.getOrderDetailsWithBook(1L))
            .doesNotThrowAnyException();

        Mockito.verify(orderDetailRepository, times(1)).countOrderDetailsByOrderId(1L);
        Mockito.verify(orderDetailRepository, times(1)).queryAllDetailsByOrderId(1L);

    }

}
