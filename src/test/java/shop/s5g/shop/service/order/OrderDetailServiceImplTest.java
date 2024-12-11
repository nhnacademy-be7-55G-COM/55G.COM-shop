package shop.s5g.shop.service.order;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static shop.s5g.shop.test.domain.TestConstants.TEST_UUID;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.dto.order.OrderDetailWithBookResponseDto;
import shop.s5g.shop.entity.order.Order;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.entity.order.OrderDetailType;
import shop.s5g.shop.exception.ResourceNotFoundException;
import shop.s5g.shop.exception.order.OrderDetailsNotExistException;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.repository.order.OrderDetailTypeRepository;
import shop.s5g.shop.repository.order.OrderRepository;
import shop.s5g.shop.service.order.impl.OrderDetailServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrderDetailServiceImplTest {
    @Mock
    OrderDetailRepository orderDetailRepository;
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderDetailTypeRepository orderDetailTypeRepository;

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
        Mockito.verify(orderDetailRepository, times(1)).countOrderDetailsByOrderId(1L);
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
    Order mockOrder = mock(Order.class);

    @Test
    void getOrderDetailsWithBookUUIDTest() {
        final String uuid = TEST_UUID;
        final long orderId = 1L;
        when(mockOrder.getId()).thenReturn(orderId);
        when(orderRepository.findOrderByUuid(anyString())).thenReturn(Optional.of(mockOrder));
        when(orderDetailRepository.queryAllDetailsByOrderId(anyLong())).thenReturn(List.of());

        assertThatCode(() -> service.getOrderDetailsWithBook(uuid))
            .doesNotThrowAnyException();

        verify(orderRepository, times(1)).findOrderByUuid(uuid);
        verify(orderDetailRepository, times(1)).queryAllDetailsByOrderId(orderId);
    }

    @Test
    void getOrderDetailsWithBookUUIDFailTest() {
        final String uuid = TEST_UUID;
        when(orderRepository.findOrderByUuid(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getOrderDetailsWithBook(uuid))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(orderRepository, times(1)).findOrderByUuid(uuid);
        verify(orderDetailRepository, never()).queryAllDetailsByOrderId(anyLong());
    }

    @Test
    void changeOrderDetailTypeTest() {
        final String type = "CONFIRM";
        final long detailId = 1L;

        OrderDetail mockDetail = mock(OrderDetail.class);
        OrderDetailType mockType = mock(OrderDetailType.class);
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.of(mockDetail));
        when(orderDetailTypeRepository.findStatusByName(anyString())).thenReturn(mockType);

        assertThatCode(() -> service.changeOrderDetailType(detailId, type))
            .doesNotThrowAnyException();

        verify(orderDetailRepository, times(1)).findById(detailId);
        verify(orderDetailTypeRepository, times(1)).findStatusByName(type);
        verify(mockDetail, times(1)).setOrderDetailType(mockType);
    }

    @Test
    void changeOrderDetailTypeDetailNotFoundTest() {
        final String type = "CONFIRM";
        final long detailId = 1L;

        OrderDetail mockDetail = mock(OrderDetail.class);
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.changeOrderDetailType(detailId, type))
            .isInstanceOf(OrderDetailsNotExistException.class)
            .hasMessageContaining(String.valueOf(detailId));

        verify(orderDetailRepository, times(1)).findById(detailId);
        verify(orderDetailTypeRepository, never()).findStatusByName(anyString());
        verify(mockDetail, never()).setOrderDetailType(any());
    }

}
