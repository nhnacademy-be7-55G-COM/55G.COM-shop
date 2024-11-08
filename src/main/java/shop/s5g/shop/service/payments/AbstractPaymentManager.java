package shop.s5g.shop.service.payments;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.order.OrderCreateRequestDto;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.order.OrderRepository;
import shop.s5g.shop.service.point.PointHistoryService;

public abstract class AbstractPaymentManager {
    private BookRepository bookRepository;
    private PointHistoryService pointHistoryService;
    private OrderRepository orderRepository;

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Autowired
    public void setPointHistoryService(PointHistoryService pointHistoryService) {
        this.pointHistoryService = pointHistoryService;
    }

    // TODO: 비회원 전용 메소드도 필요함.
    @Transactional
    public <T> T confirmPayment(
        long memberId,
        long orderDataId,       // TODO: orderDataId로 링크해야함.
        OrderCreateRequestDto order,
        Map<String, Object> request,
        Class<T> responseType
    ) {
        T object = responseType.cast(confirmPaymentAdapter(request));

//        // TODO: book 데이터가 생기면 활성화.
//        for (OrderDetailCreateRequestDto detail: createRequest.cartList()) {
//
//            Book book = bookRepository.findById(detail.bookId()).orElseThrow(
//                () -> new BookResourceNotFoundException("book does not exist: " +detail.bookId())
//            );
//
//            // TODO: stock - quantity가 음수인지 아닌지는 프론트에서도 판단해야함!
//            book.setStock(book.getStock() - detail.quantity());
//
//            // TODO: 여기서 포인트 계산하거나 프론트가 넘겨줘야함!
//            pointHistoryService.createPointHistory(
//                memberId, new PointHistoryCreateRequestDto("구매", 5000L)
//            );
//        }

        return object;
    }

    @Transactional
    public <T> T cancelPayment(long memberId, Map<String, Object> request) {
        // Not implemented yet
        // TODO: 주문 부분 취소 구현
        return null;
    }

    // TODO: 주문에 연관된 payment 레코드 생성도 여기서
    protected abstract Object confirmPaymentAdapter(Map<String, Object> request);
    protected abstract Object cancelPaymentAdapter(String paymentKey, Map<String, Object> request);
}
