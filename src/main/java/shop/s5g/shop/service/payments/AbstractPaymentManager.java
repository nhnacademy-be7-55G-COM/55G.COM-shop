package shop.s5g.shop.service.payments;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.point.PointHistoryCreateRequestDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.Payment;
import shop.s5g.shop.entity.member.Customer;
import shop.s5g.shop.entity.order.Order;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.entity.order.OrderDetailType;
import shop.s5g.shop.entity.order.OrderDetailType.Type;
import shop.s5g.shop.entity.point.PointSource.Name;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.exception.order.OrderDetailsNotExistException;
import shop.s5g.shop.exception.order.OrderDoesNotProceedException;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.repository.order.OrderDetailTypeRepository;
import shop.s5g.shop.repository.order.OrderRepository;
import shop.s5g.shop.repository.payments.TossPaymentRepository;
import shop.s5g.shop.service.delivery.DeliveryService;
import shop.s5g.shop.service.point.PointHistoryService;

@Slf4j
public abstract class AbstractPaymentManager {
    private PointHistoryService pointHistoryService;
    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;
    private OrderDetailTypeRepository orderDetailTypeRepository;
    private DeliveryService deliveryService;
    private TossPaymentRepository paymentRepository;

    // TODO: 비회원 전용 메소드도 필요함.
    @Transactional
    public <T> T confirmPayment(
        long orderDataId,
        long usedPoint,
        Map<String, Object> request,
        Class<T> responseType
    ) {
        long accPrice = 0L;
        Order order = orderRepository.findOrderByIdFetch(orderDataId);

        // 지연 로딩, 배치사이즈 30
        List<OrderDetail> details = order.getOrderDetails();

        for (OrderDetail detail: details) {
            Book book = detail.getBook();

            // TODO: stock - quantity가 음수인지 아닌지는 프론트에서도 판단해야함!
            int rest = book.getStock() - detail.getQuantity();
            if (rest < 0) {
                throw new OrderDoesNotProceedException("재고가 부족함!");
            }
            book.setStock(book.getStock() - detail.getQuantity());

            accPrice += detail.getAccumulationPrice();
        }

        Customer customer = order.getCustomer();
        // 회원 only
        if (customer.getMember() != null) {
            if (usedPoint > 0) {    // 포인트를 사용해 구매함
                pointHistoryService.createPointHistory(customer.getCustomerId(), new PointHistoryCreateRequestDto(
                    Name.PURCHASE.getDataName(), -usedPoint));
            } else {
                pointHistoryService.createPointHistory(
                    customer.getCustomerId(), new PointHistoryCreateRequestDto(Name.PURCHASE.getDataName(), accPrice)
                );
            }
        }
        T object = responseType.cast(confirmPaymentAdapter(orderDataId, request));

        return object;
    }

    @Transactional
    public <T> T cancelPayment(
        long orderDetailId,
        Map<String, Object> request,        // cancelReason 은 이미 담겨있음. -> 확장성을 위해서 나중에 뜯어내야 할지도?
        Class<T> responseType
    ) {
        // Not implemented yet
        // TODO: 주문 부분 취소 구현
        // TODO: 전체적으로 N+1 문제가 발생하고 있음.
        OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId).orElseThrow(
            () -> new OrderDetailsNotExistException("주문을 찾을 수 없습니다")
        );

        // TODO: BadRequest 말고 다른거.
        if (!orderDetail.getOrderDetailType().getName().equals(Type.COMPLETE.name())) {
            throw new BadRequestException("취소할 수 없는 주문입니다.");
        }

        // TODO: payment가 가지고 있는 값이랑 환불할 금액의 차이가 음수가 되지 않아야함.
        // TODO: 환불 이후 payment의 값을 차감해야함.
        Payment payment = paymentRepository.findByOrderRelationId(orderDetail.getOrder().getId()).orElseThrow(
            () -> new OrderDoesNotProceedException("해당 주문은 결제된 적이 없습니다.")
        );
        // cancelReason 은 생략.
        long totalPrice = orderDetail.getTotalPrice();
        request.put("cancelAmount", totalPrice);

        T object = responseType.cast(cancelPaymentAdapter(payment.getPaymentKey(), request));
        payment.setAmount(payment.getAmount() - totalPrice);

        OrderDetailType type = orderDetailTypeRepository.findStatusByName(Type.CANCEL);

        orderDetail.setOrderDetailType(type);

        Book book = orderDetail.getBook();

        int stock = book.getStock();
        book.setStock(stock + orderDetail.getQuantity());
        // TODO: fetch join 필요?
        deliveryService.deliveryCheckForCancel(orderDetail.getOrder().getId());
        return object;
    }

    // TODO: 주문에 연관된 payment 레코드 생성도 여기서
    protected abstract Object confirmPaymentAdapter(long orderDataId, Map<String, Object> request);

    /**
     *
     * @param paymentKey  취소 대상의 payment 키
     * @param request     String cancelReason: 취소사유, long cancelAmount: 취소할 만큼의 가격
     * @return  호출한 파라미터의 Class 타입 DTO
     */
    protected abstract Object cancelPaymentAdapter(String paymentKey, Map<String, Object> request);

    @Autowired
    public void setPointHistoryService(PointHistoryService pointHistoryService) {
        this.pointHistoryService = pointHistoryService;
    }

    @Autowired
    public void setOrderDetailRepository(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setOrderDetailTypeRepository(OrderDetailTypeRepository orderDetailTypeRepository) {
        this.orderDetailTypeRepository = orderDetailTypeRepository;
    }

    @Autowired
    public void setDeliveryService(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Autowired
    public void setPaymentRepository(TossPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
