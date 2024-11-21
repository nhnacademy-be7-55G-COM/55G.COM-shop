package shop.s5g.shop.service.payments;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import shop.s5g.shop.adapter.TossPaymentsAdapter;
import shop.s5g.shop.dto.payments.TossPaymentsDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.order.Order;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.repository.order.OrderRepository;
import shop.s5g.shop.repository.payments.TossPaymentRepository;
import shop.s5g.shop.service.payments.impl.TossPaymentsManagerImpl;
import shop.s5g.shop.service.point.PointHistoryService;

@ExtendWith(MockitoExtension.class)
class TossPaymentsManagerImplTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    TossPaymentsAdapter adapter;
    @Mock
    TossPaymentRepository tossPaymentRepository;
    @Mock
    PointHistoryService pointHistoryService;
    @Mock
    OrderDetailRepository orderDetailRepository;

    @InjectMocks
    TossPaymentsManagerImpl manager;

    long orderDataId = 1L;
    Map<String, Object> request = new HashMap<>();
    OrderDetail orderDetail = mock(OrderDetail.class);
    Book book = mock(Book.class);
    TossPaymentsDto mockDto = mock(TossPaymentsDto.class);
    ResponseEntity<TossPaymentsDto> mockResponse = ResponseEntity.ok(mockDto);
    Order order = mock(Order.class);

    @BeforeEach
    void initRequest() {
        request.put("paymentKey", "djJ3132Jcd90");
        request.put("orderId", "9vVf3kud0D");
        request.put("amount", "5000");
    }

    @Test
    @Disabled
    void confirmPaymentAdapterTest() { // TODO: 테스트 보류
        when(orderDetail.getBook()).thenReturn(book);
        when(orderDetail.getQuantity()).thenReturn(5);
        when(orderDetail.getAccumulationPrice()).thenReturn(100);
        when(orderDetailRepository.fetchOrderDetailsByOrderId(anyLong())).thenReturn(List.of(orderDetail));

        when(book.getBookId()).thenReturn(1L);
        when(book.getStock()).thenReturn(100);

        when(adapter.confirm(any())).thenReturn(mockResponse);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

//        assertThatCode(() -> manager.confirmPayment(
//            1L, orderDataId, request, TossPaymentsDto.class
//        )).doesNotThrowAnyException();

        verify(orderDetailRepository, times(1)).fetchOrderDetailsByOrderId(orderDataId);
        verify(orderDetail, times(1)).getBook();
        verify(orderDetailRepository, times(1)).fetchOrderDetailsByOrderId(orderDataId);
        verify(orderDetail, times(1)).getBook();
        verify(book, times(1)).setStock(anyInt());
        verify(pointHistoryService, times(1)).createPointHistory(eq(1L), any());

        verify(tossPaymentRepository, times(1)).save(any());

    }
}
