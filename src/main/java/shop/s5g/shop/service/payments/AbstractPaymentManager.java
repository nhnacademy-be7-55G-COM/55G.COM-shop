package shop.s5g.shop.service.payments;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.point.PointHistoryCreateRequestDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.exception.book.BookResourceNotFoundException;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.service.point.PointHistoryService;

public abstract class AbstractPaymentManager {
    private BookRepository bookRepository;
    private PointHistoryService pointHistoryService;
//    private OrderRepository orderRepository;
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

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
        long orderDataId,       // TODO: orderDataId로 링크해야함.
        Map<String, Object> request,
        Class<T> responseType
    ) {
        long accPrice = 0L; // TODO: 포인트 적립률을 가져와야함!
        List<OrderDetail> details = orderDetailRepository.fetchOrderDetailsByOrderId(orderDataId);

        // TODO: book 데이터가 생기면 활성화.
        for (OrderDetail detail: details) {

            Book book = bookRepository.findById(detail.getBook().getBookId()).orElseThrow(
                () -> new BookResourceNotFoundException("book does not exist: " +detail.getBook().getBookId())
            );

            // TODO: stock - quantity가 음수인지 아닌지는 프론트에서도 판단해야함!
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
