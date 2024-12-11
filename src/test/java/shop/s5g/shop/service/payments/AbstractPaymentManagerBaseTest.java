package shop.s5g.shop.service.payments;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.Payment;
import shop.s5g.shop.entity.member.Customer;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.entity.order.Order;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.entity.order.OrderDetailType;
import shop.s5g.shop.entity.order.OrderDetailType.Type;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.exception.order.OrderDetailsNotExistException;
import shop.s5g.shop.exception.order.OrderDoesNotProceedException;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.repository.order.OrderDetailTypeRepository;
import shop.s5g.shop.repository.order.OrderRepository;
import shop.s5g.shop.repository.payments.TossPaymentRepository;
import shop.s5g.shop.service.delivery.DeliveryService;
import shop.s5g.shop.service.point.PointHistoryService;

@ExtendWith(MockitoExtension.class)
class AbstractPaymentManagerBaseTest {
    @Mock
    PointHistoryService pointHistoryService;
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderDetailRepository orderDetailRepository;
    @Mock
    OrderDetailTypeRepository orderDetailTypeRepository;
    @Mock
    DeliveryService deliveryService;
    @Mock
    TossPaymentRepository paymentRepository;

    AbstractPaymentManager manager;

    ResponseEntity<HttpStatus> mockResponse = mock(ResponseEntity.class);
    Map<String, Object> request = new HashMap<>();
    Class<?> responseType = ResponseEntity.class;

    @BeforeEach
    void init() {
        manager = spy(AbstractPaymentManager.class);
        manager.setPointHistoryService(pointHistoryService);
        manager.setDeliveryService(deliveryService);
        manager.setOrderDetailRepository(orderDetailRepository);
        manager.setOrderRepository(orderRepository);
        manager.setPaymentRepository(paymentRepository);
        manager.setOrderDetailTypeRepository(orderDetailTypeRepository);
    }

    Order mockOrder = mock(Order.class);
    Book mockBook = mock(Book.class);
    OrderDetail detail = new OrderDetail(
        1L, mockBook, mockOrder, null, null,
        2, 5000L, 500
    );

    Member mockMember = mock(Member.class);
    Customer customer = new Customer(
        1L, null, "testName", "01012345667",
        "email@example.org", true, mockMember
    );

    @Test
    void confirmPaymentAndAccumulatePoint() {
        when(manager.confirmPaymentAdapter(anyLong(), any())).thenReturn(mockResponse);
        when(orderRepository.findOrderByIdFetch(anyLong())).thenReturn(mockOrder);
        when(mockOrder.getOrderDetails()).thenReturn(List.of(detail));
        when(mockOrder.getCustomer()).thenReturn(customer);
        when(mockBook.getStock()).thenReturn(2);

        assertThat(manager.confirmPayment(1L, 0L, request, responseType))
            .isInstanceOf(responseType)
            .isSameAs(mockResponse);

        verify(pointHistoryService, times(1)).createPointHistory(eq(1L), any());
        verify(mockBook, times(1)).setStock(anyInt());
    }

    @Test
    void confirmPaymentAndUsePoint() {
        when(manager.confirmPaymentAdapter(anyLong(), any())).thenReturn(mockResponse);
        when(orderRepository.findOrderByIdFetch(anyLong())).thenReturn(mockOrder);
        when(mockOrder.getOrderDetails()).thenReturn(List.of(detail));
        when(mockOrder.getCustomer()).thenReturn(customer);
        when(mockBook.getStock()).thenReturn(2);

        assertThat(manager.confirmPayment(1L, 5000L, request, responseType))
            .isInstanceOf(responseType)
            .isSameAs(mockResponse);

        verify(pointHistoryService, times(1)).createPointHistory(eq(1L), any());
        verify(mockBook, times(1)).setStock(anyInt());
        verify(manager, times(1)).confirmPaymentAdapter(anyLong(), any());
    }

    @Test
    void confirmPayment_NotEnoughStock() {
        when(orderRepository.findOrderByIdFetch(anyLong())).thenReturn(mockOrder);
        when(mockOrder.getOrderDetails()).thenReturn(List.of(detail));
        when(mockBook.getStock()).thenReturn(1);

        assertThatThrownBy(() -> manager.confirmPayment(1L, 5000L, request, responseType))
            .isInstanceOf(OrderDoesNotProceedException.class)
            .hasMessageContaining("부족");

        verify(pointHistoryService, never()).createPointHistory(eq(1L), any());
        verify(mockBook, never()).setStock(anyInt());
        verify(manager, never()).confirmPaymentAdapter(anyLong(), anyMap());
    }

    @Test
    void cancelPayment_NotFoundDetail() {
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> manager.cancelPayment(1L, request, responseType))
            .isInstanceOf(OrderDetailsNotExistException.class);

        verify(orderDetailRepository, times(1)).findById(anyLong());
        verify(manager, never()).cancelPaymentAdapter(anyString(), anyMap());
    }

    @Test
    void cancelPayment_NotFoundPayment() {
        ReflectionTestUtils.setField(detail, "orderDetailType", completeType);
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.of(detail));
        when(paymentRepository.findByOrderRelationId(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> manager.cancelPayment(1L, request, responseType))
            .isInstanceOf(OrderDoesNotProceedException.class);

        verify(paymentRepository, times(1)).findByOrderRelationId(anyLong());
        verify(manager, never()).cancelPaymentAdapter(anyString(), anyMap());
    }

    Payment payment = new Payment(1L, mockOrder, "testPaymentKey", "KRW", detail.getTotalPrice(), "testOrderId");
    OrderDetailType completeType = new OrderDetailType(1L, Type.COMPLETE.name());
    OrderDetailType confirmType = new OrderDetailType(2L, Type.CONFIRM.name());

    @Test
    void cancelPaymentTest() {
        final int nowStock = 3;

        ReflectionTestUtils.setField(detail, "orderDetailType", completeType);
        when(mockBook.getStock()).thenReturn(nowStock);
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.of(detail));
        when(paymentRepository.findByOrderRelationId(anyLong())).thenReturn(Optional.of(payment));
        when(manager.cancelPaymentAdapter(anyString(), anyMap())).thenReturn(mockResponse);
        when(orderDetailTypeRepository.findStatusByName(any(OrderDetailType.Type.class))).thenReturn(completeType);

        assertThat(manager.cancelPayment(1L, request, responseType))
            .isInstanceOf(responseType)
            .isSameAs(mockResponse);

        verify(paymentRepository, times(1)).findByOrderRelationId(anyLong());
        verify(deliveryService, times(1)).deliveryCheckForCancel(anyLong());
        verify(manager, times(1)).cancelPaymentAdapter(payment.getPaymentKey(), request);
        verify(mockBook, times(1)).setStock(nowStock + detail.getQuantity());
    }
    @Test
    void cancelPayment_CannotCancelType() {
        final int nowStock = 3;

        ReflectionTestUtils.setField(detail, "orderDetailType", confirmType);
        when(orderDetailRepository.findById(anyLong())).thenReturn(Optional.of(detail));

        assertThatThrownBy(() -> manager.cancelPayment(1L, request, responseType))
            .isInstanceOf(BadRequestException.class);

        verify(manager, never()).cancelPaymentAdapter(payment.getPaymentKey(), request);
        verify(mockBook, never()).setStock(nowStock + detail.getQuantity());
    }
}
