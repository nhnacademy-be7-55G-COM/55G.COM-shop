package shop.S5G.shop.service.order.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.delivery.DeliveryCreateRequestDto;
import shop.S5G.shop.dto.order.OrderCreateRequestDto;
import shop.S5G.shop.dto.order.OrderCreateResponseDto;
import shop.S5G.shop.dto.order.OrderDetailCreateRequestDto;
import shop.S5G.shop.dto.order.OrderWithDetailResponseDto;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.entity.member.Customer;
import shop.S5G.shop.entity.order.Delivery;
import shop.S5G.shop.entity.order.DeliveryFee;
import shop.S5G.shop.entity.order.Order;
import shop.S5G.shop.entity.order.OrderDetail;
import shop.S5G.shop.entity.order.OrderDetailType;
import shop.S5G.shop.entity.order.WrappingPaper;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.exception.BookException.BookResourceNotFoundException;
import shop.S5G.shop.exception.EssentialDataNotFoundException;
import shop.S5G.shop.exception.member.CustomerNotFoundException;
import shop.S5G.shop.exception.order.WrappingPaperDoesNotExistsException;
import shop.S5G.shop.repository.book.BookRepository;
import shop.S5G.shop.repository.member.CustomerRepository;
import shop.S5G.shop.repository.order.DeliveryFeeRepository;
import shop.S5G.shop.repository.order.DeliveryRepository;
import shop.S5G.shop.repository.order.OrderDetailRepository;
import shop.S5G.shop.repository.order.OrderDetailTypeRepository;
import shop.S5G.shop.repository.order.OrderRepository;
import shop.S5G.shop.repository.order.WrappingPaperRepository;
import shop.S5G.shop.service.order.OrderService;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final DeliveryFeeRepository deliveryFeeRepository;
    private final CustomerRepository customerRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final BookRepository bookRepository;
    private final WrappingPaperRepository wrappingPaperRepository;
    private final OrderDetailTypeRepository orderDetailTypeRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Order> findAllByCustomerId(long customerId, Pageable pageable) {
        return orderRepository.findAllByCustomerCustomerId(customerId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderWithDetailResponseDto> queryAllOrdersByCustomerId(long customerId) {
        return orderRepository.findOrdersByCustomerId(customerId);
    }

    // TODO: 포인트 사용, 재고처리에 대해 고민해야함.
    @Override
    public OrderCreateResponseDto createOrder(OrderCreateRequestDto requestDto) {
        DeliveryCreateRequestDto deliveryDto = requestDto.delivery();

        DeliveryFee fee = deliveryFeeRepository.findById(deliveryDto.deliveryFeeId()).orElseThrow(
            () -> new BadRequestException("Cannot find delivery fee data")
        );
        Delivery delivery = deliveryRepository.save(
            new Delivery(deliveryDto.address(), deliveryDto.receivedDate(), (int)fee.getFee())
        );

        Customer customer = customerRepository.findById(requestDto.customerId()).orElseThrow(
            () -> new CustomerNotFoundException("Customer not found given id: "+requestDto.customerId())
        );

        // order 생성
        Order order = orderRepository.save(
            new Order(customer, delivery, requestDto.netPrice(), requestDto.totalPrice())
        );

        // TODO: Toss payment 결제

        // orderDetail 생성
        linkOrderDetails(order, requestDto.cartList());
        return OrderCreateResponseDto.of(order);
    }

    private void linkOrderDetails(Order order, List<OrderDetailCreateRequestDto> details) {
        for (OrderDetailCreateRequestDto detail: details) {
            Book book = bookRepository.findById(detail.bookId()).orElseThrow(
                () -> new BookResourceNotFoundException("Book not found: "+detail.bookId())
            );
            WrappingPaper wrappingPaper = wrappingPaperRepository.findById(detail.wrappingPaperId()).orElseThrow(
                () -> new WrappingPaperDoesNotExistsException(detail.wrappingPaperId())
            );
            OrderDetailType type = orderDetailTypeRepository.findByName("WAIT").orElseThrow(
                () -> new EssentialDataNotFoundException("Order detail type error")
            );

            OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .book(book)
                .wrappingPaper(wrappingPaper)
                .orderDetailType(type)
                .quantity(detail.quantity())
                .totalPrice(detail.totalPrice())
                .accumulationPrice(detail.accumulationPrice())
                .build();
            orderDetailRepository.save(orderDetail);
        }
    }
}
