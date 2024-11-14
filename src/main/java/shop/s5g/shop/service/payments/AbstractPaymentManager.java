package shop.s5g.shop.service.payments;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.point.PointHistoryCreateRequestDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.exception.order.OrderDoesNotProceedException;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.service.point.PointHistoryService;

public abstract class AbstractPaymentManager {
    private PointHistoryService pointHistoryService;
//    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    public void setPointHistoryService(PointHistoryService pointHistoryService) {
        this.pointHistoryService = pointHistoryService;
    }

    @Autowired
    public void setOrderDetailRepository(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    // TODO: 비회원 전용 메소드도 필요함.
    @Transactional
    public <T> T confirmPayment(
        long memberId,
        long orderDataId,
        Map<String, Object> request,
        Class<T> responseType
    ) {
        long accPrice = 0L;
        List<OrderDetail> details = orderDetailRepository.fetchOrderDetailsByOrderId(orderDataId);

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
        pointHistoryService.createPointHistory(
            memberId, new PointHistoryCreateRequestDto("구매", accPrice)
        );
        T object = responseType.cast(confirmPaymentAdapter(orderDataId, request));

        return object;
    }

    @Transactional
    public <T> T cancelPayment(long memberId, Map<String, Object> request) {
        // Not implemented yet
        // TODO: 주문 부분 취소 구현
        return null;
    }

    // TODO: 주문에 연관된 payment 레코드 생성도 여기서
    protected abstract Object confirmPaymentAdapter(long orderDataId, Map<String, Object> request);
    protected abstract Object cancelPaymentAdapter(String paymentKey, Map<String, Object> request);
}
