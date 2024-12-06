package shop.s5g.shop.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.delivery.DeliveryCreateRequestDto;
import shop.s5g.shop.dto.order.OrderCreateRequestDto;
import shop.s5g.shop.dto.order.OrderDetailCreateRequestDto;
import shop.s5g.shop.dto.order.OrderQueryRequestDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.delivery.Delivery;
import shop.s5g.shop.entity.delivery.DeliveryFee;
import shop.s5g.shop.entity.delivery.DeliveryStatus;
import shop.s5g.shop.entity.member.Customer;
import shop.s5g.shop.entity.order.Order;
import shop.s5g.shop.entity.order.OrderDetailType;
import shop.s5g.shop.entity.order.OrderDetailType.Type;
import shop.s5g.shop.entity.order.WrappingPaper;
import shop.s5g.shop.exception.EssentialDataNotFoundException;
import shop.s5g.shop.exception.ResourceNotFoundException;
import shop.s5g.shop.exception.book.BookResourceNotFoundException;
import shop.s5g.shop.exception.member.CustomerNotFoundException;
import shop.s5g.shop.exception.order.WrappingPaperDoesNotExistsException;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.delivery.DeliveryFeeRepository;
import shop.s5g.shop.repository.delivery.DeliveryRepository;
import shop.s5g.shop.repository.delivery.DeliveryStatusRepository;
import shop.s5g.shop.repository.member.CustomerRepository;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.repository.order.OrderDetailTypeRepository;
import shop.s5g.shop.repository.order.OrderRepository;
import shop.s5g.shop.repository.order.WrappingPaperRepository;
import shop.s5g.shop.service.order.impl.OrderServiceImpl;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    DeliveryRepository deliveryRepository;
    @Mock
    DeliveryFeeRepository deliveryFeeRepository;
    @Mock
    CustomerRepository customerRepository;
    @Mock
    OrderDetailRepository orderDetailRepository;
    @Mock
    BookRepository bookRepository;
    @Mock
    WrappingPaperRepository wrappingPaperRepository;
    @Mock
    OrderDetailTypeRepository orderDetailTypeRepository;
    @Mock
    DeliveryStatusRepository deliveryStatusRepository;

    @InjectMocks
    OrderServiceImpl orderService;

    @Test
    void getAllOrdersWithDetailTest() {
        assertThatCode(() -> orderService.getAllOrdersWithDetail(1L))
            .doesNotThrowAnyException();
    }

    @Test
    void findAllByCustomerIdTest() {
        assertThatCode(() -> orderService.findAllByCustomerId(1L, mock(Pageable.class)))
            .doesNotThrowAnyException();
        verify(orderRepository, times(1)).findAllByCustomerCustomerId(eq(1L), any());
    }

    @Test
    void deactivateOrderFailTest() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.deactivateOrder(1L))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void deactivateOrderSuccessTest() {
        Order order = mock(Order.class);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        assertThatCode(() -> orderService.deactivateOrder(1L))
            .doesNotThrowAnyException();

        verify(orderRepository, times(1)).findById(1L);
        verify(order, times(1)).setActive(false);
    }

    @Test
    void getAllOrdersBetweenDatesTest() {
        OrderQueryRequestDto query = mock(OrderQueryRequestDto.class);
        assertThatCode(() -> orderService.getAllOrdersBetweenDates(1L, query))
            .doesNotThrowAnyException();
        verify(query, times(1)).startDate();
        verify(query, times(1)).endDate();
    }

    DeliveryCreateRequestDto deliveryReq = new DeliveryCreateRequestDto(
        "주소", 1L, LocalDate.of(2022,2,2), "아무개"
    );

    OrderDetailCreateRequestDto detail = new OrderDetailCreateRequestDto(
        1L, 2L, 3, 4000, 200
    );

    OrderCreateRequestDto request = new OrderCreateRequestDto(
        deliveryReq, List.of(detail), 0L, 0L, 0L
    );

    @Test
    void createOrderSearchFailTest() {
        DeliveryFee fee = mock(DeliveryFee.class);
        DeliveryStatus status = mock(DeliveryStatus.class);
        Delivery delivery = mock(Delivery.class);

        when(deliveryFeeRepository.findById(anyLong())).thenReturn(Optional.of(fee));
        when(deliveryStatusRepository.findStatusByName(anyString())).thenReturn(status);
        when(deliveryRepository.save(any())).thenReturn(delivery);
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(1L, request))
            .isInstanceOf(CustomerNotFoundException.class);

        verify(orderRepository, never()).save(any());

        when(deliveryStatusRepository.findStatusByName(anyString())).thenThrow(
            EssentialDataNotFoundException.class
        );

        assertThatThrownBy(() -> orderService.createOrder(1L, request))
            .isInstanceOf(EssentialDataNotFoundException.class);

        verify(orderRepository, never()).save(any());

        when(deliveryFeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(1L, request))
            .isInstanceOf(EssentialDataNotFoundException.class);

        verify(orderRepository, never()).save(any());
    }

    @Test
    void linkOrderDetailsFailTest() {
        DeliveryFee fee = mock(DeliveryFee.class);
        DeliveryStatus status = mock(DeliveryStatus.class);
        Delivery delivery = mock(Delivery.class);
        Customer customer = mock(Customer.class);
        Book book = mock(Book.class);
        WrappingPaper wrappingPaper = mock(WrappingPaper.class);

        when(deliveryFeeRepository.findById(anyLong())).thenReturn(Optional.of(fee));
        when(deliveryStatusRepository.findStatusByName(anyString())).thenReturn(status);
        when(deliveryRepository.save(any())).thenReturn(delivery);
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(wrappingPaperRepository.findById(anyLong())).thenReturn(Optional.of(wrappingPaper));
        when(orderDetailTypeRepository.findStatusByName(Type.COMPLETE)).thenThrow(
            EssentialDataNotFoundException.class
        );
//      --------- 주문 타입 비활성화 -----------
        assertThatThrownBy(() -> orderService.createOrder(1L, request))
            .isInstanceOf(EssentialDataNotFoundException.class);

        verify(orderRepository, atLeastOnce()).save(any());
        verify(orderDetailRepository, never()).save(any());
//      --------- 포장지 비활성화 -----------
        when(wrappingPaperRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(1L, request))
            .isInstanceOf(WrappingPaperDoesNotExistsException.class);

        verify(orderRepository, atLeastOnce()).save(any());
        verify(orderDetailRepository, never()).save(any());
//      --------- 책 비활성화 -----------
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(1L, request))
            .isInstanceOf(BookResourceNotFoundException.class);

        verify(orderRepository, atLeastOnce()).save(any());
        verify(orderDetailRepository, never()).save(any());

    }

    @Test
    void createOrderSuccessTest() {
        DeliveryFee fee = mock(DeliveryFee.class);
        DeliveryStatus status = mock(DeliveryStatus.class);
        Delivery delivery = mock(Delivery.class);
        Customer customer = mock(Customer.class);
        Book book = mock(Book.class);
        WrappingPaper wrappingPaper = mock(WrappingPaper.class);
        OrderDetailType type = mock(OrderDetailType.class);
        Order order = mock(Order.class);
        when(order.getId()).thenReturn(123L);

        when(deliveryFeeRepository.findById(anyLong())).thenReturn(Optional.of(fee));
        when(deliveryStatusRepository.findStatusByName(anyString())).thenReturn(status);
        when(deliveryRepository.save(any())).thenReturn(delivery);
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(wrappingPaperRepository.findById(anyLong())).thenReturn(Optional.of(wrappingPaper));
        when(orderDetailTypeRepository.findStatusByName(Type.COMPLETE)).thenReturn(type);

        when(orderRepository.save(any())).thenReturn(order);

        assertThat(orderService.createOrder(1L, request))
            .hasFieldOrPropertyWithValue("orderId", 123L);

        verify(orderDetailRepository, times(request.cartList().size())).save(any());
        verify(orderRepository, times(1)).save(any());
    }
}
