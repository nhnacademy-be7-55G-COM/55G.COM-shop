package shop.s5g.shop.service.order.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.delivery.DeliveryCreateRequestDto;
import shop.s5g.shop.dto.order.OrderAdminTableView;
import shop.s5g.shop.dto.order.OrderCreateRequestDto;
import shop.s5g.shop.dto.order.OrderCreateResponseDto;
import shop.s5g.shop.dto.order.OrderDetailCreateRequestDto;
import shop.s5g.shop.dto.order.OrderQueryFilterDto;
import shop.s5g.shop.dto.order.OrderQueryRequestDto;
import shop.s5g.shop.dto.order.OrderWithDetailResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.delivery.DeliveryStatus.Type;
import shop.s5g.shop.entity.member.Customer;
import shop.s5g.shop.entity.delivery.Delivery;
import shop.s5g.shop.entity.delivery.DeliveryFee;
import shop.s5g.shop.entity.delivery.DeliveryStatus;
import shop.s5g.shop.entity.order.Order;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.entity.order.OrderDetailType;
import shop.s5g.shop.entity.order.WrappingPaper;
import shop.s5g.shop.exception.EssentialDataNotFoundException;
import shop.s5g.shop.exception.ResourceNotFoundException;
import shop.s5g.shop.exception.book.BookResourceNotFoundException;
import shop.s5g.shop.exception.member.CustomerNotFoundException;
import shop.s5g.shop.exception.order.WrappingPaperDoesNotExistsException;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.member.CustomerRepository;
import shop.s5g.shop.repository.delivery.DeliveryFeeRepository;
import shop.s5g.shop.repository.delivery.DeliveryRepository;
import shop.s5g.shop.repository.delivery.DeliveryStatusRepository;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.repository.order.OrderDetailTypeRepository;
import shop.s5g.shop.repository.order.OrderRepository;
import shop.s5g.shop.repository.order.WrappingPaperRepository;
import shop.s5g.shop.service.order.OrderService;

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
    private final DeliveryStatusRepository deliveryStatusRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Order> findAllByCustomerId(long customerId, Pageable pageable) {
        return orderRepository.findAllByCustomerCustomerId(customerId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderWithDetailResponseDto> getAllOrdersWithDetail(long customerId) {
        return orderRepository.findOrdersByCustomerId(customerId);
    }

    // TODO: 포인트 사용, 재고처리에 대해 고민해야함. -> payment에서 해결할것.
    @Override
    public OrderCreateResponseDto createOrder(long customerId, OrderCreateRequestDto requestDto) {
        DeliveryCreateRequestDto deliveryDto = requestDto.delivery();

        DeliveryFee fee = deliveryFeeRepository.findById(deliveryDto.deliveryFeeId()).orElseThrow(
            () -> new EssentialDataNotFoundException("Cannot find delivery fee data")
        );

        DeliveryStatus status = deliveryStatusRepository.findStatusByName(Type.PREPARING.name());

        Delivery delivery = deliveryRepository.save(
            new Delivery(deliveryDto.address(), deliveryDto.receivedDate(), status, fee, deliveryDto.receiverName())
        );

        Customer customer = customerRepository.findById(customerId).orElseThrow(
            () -> new CustomerNotFoundException("Customer not found given id: "+customerId)
        );

        // order 생성
        Order order = orderRepository.save(
            new Order(customer, delivery, requestDto.netPrice(), requestDto.totalPrice())
        );

        OrderDetailType.Type type = requestDto.usePoint() == 0 ? OrderDetailType.Type.COMPLETE : OrderDetailType.Type.CONFIRM;

        // orderDetail 생성
        linkOrderDetails(order, requestDto.cartList(), type);
        return OrderCreateResponseDto.of(order);
    }

    private void linkOrderDetails(Order order, List<OrderDetailCreateRequestDto> details, OrderDetailType.Type type) {
        for (OrderDetailCreateRequestDto detail: details) {
            Book book = bookRepository.findById(detail.bookId()).orElseThrow(
                () -> new BookResourceNotFoundException("Book not found: "+detail.bookId())
            );
            WrappingPaper wrappingPaper = detail.wrappingPaperId() == null ? null : wrappingPaperRepository.findById(detail.wrappingPaperId()).orElseThrow(
                () -> new WrappingPaperDoesNotExistsException(detail.wrappingPaperId())
            );
            OrderDetailType typeEntity = orderDetailTypeRepository.findStatusByName(type);

            OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .book(book)
                .wrappingPaper(wrappingPaper)
                .orderDetailType(typeEntity)
                .quantity(detail.quantity())
                .totalPrice(detail.totalPrice())
                .accumulationPrice(detail.accumulationPrice())
                .build();
            orderDetailRepository.save(orderDetail);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderWithDetailResponseDto> getAllOrdersBetweenDates(
        long customerId,
        OrderQueryRequestDto queryRequest
    ) {
        return orderRepository.findOrdersByCustomerIdBetweenDates(
            customerId, queryRequest.startDate(), queryRequest.endDate()
        );
    }

    @Override
    public void deactivateOrder(long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Given id is not available: "+orderId));
        order.setActive(false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderAdminTableView> getOrderListAdmin(OrderQueryFilterDto filter) {
        return orderRepository.findOrdersUsingFilterForAdmin(filter);
    }

    @Override
    @Transactional(readOnly = true)
    public long getCustomerIdWithOrderId(long orderId) {
        return orderRepository.findById(orderId).orElseThrow(
            () -> new ResourceNotFoundException("Order not exist")
        ).getCustomer().getCustomerId();
    }
}
