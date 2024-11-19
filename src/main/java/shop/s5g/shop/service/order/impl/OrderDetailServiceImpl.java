package shop.s5g.shop.service.order.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.order.OrderDetailWithBookResponseDto;
import shop.s5g.shop.exception.order.OrderDetailsNotExistException;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.repository.order.OrderDetailTypeRepository;
import shop.s5g.shop.service.delivery.DeliveryService;
import shop.s5g.shop.service.order.OrderDetailService;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailTypeRepository orderDetailTypeRepository;
    private final DeliveryService deliveryService;

//    private AbstractPaymentManager paymentManager;
//
//    @Autowired
//    public void setPaymentManager(@Qualifier("tossPaymentsManager") AbstractPaymentManager paymentManager) {
//        this.paymentManager = paymentManager;
//    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDetailWithBookResponseDto> getOrderDetailsWithBook(long orderId) {
        // join 이 많아서 일단 존재유무를 확인하고 날림.
        // TODO: 어떤것이 성능이 더 좋은지?
        if (orderDetailRepository.countOrderDetailsByOrderId(orderId) > 0) {
            return orderDetailRepository.queryAllDetailsByOrderId(orderId);
        } else {
            throw new OrderDetailsNotExistException(String.format("OrderDetails do not exist for [%d]", orderId));
        }
    }

    // TODO: AbstractPaymentManager의 내용물을 여기다 옮겨서 리팩토링 할 예정...
//    @Override
//    public void cancelOrderDetail(long orderDetailId) {
//        // TODO: 만약 모든 주문이 취소되었다면 배송도 완료 혹은 취소되어야 함.
//        // TODO: 주문을 취소하고 재고를 그만큼 다시 되돌려야함.
//        OrderDetail orderDetail =  orderDetailRepository.findById(orderDetailId).orElseThrow(
//            () -> new OrderDetailsNotExistException("주문을 찾을 수 없습니다.")
//        );
//
//        if (!orderDetail.getOrderDetailType().getName().equals(Type.COMPLETE.name())) {
//            throw new BadRequestException("주문 완료 상태만 취소가 가능합니다.");
//        }
//
//        OrderDetailType type = orderDetailTypeRepository.findStatusByName(Type.CANCEL);
//
//        orderDetail.setOrderDetailType(type);
//
//        // TODO: fetch join 필요?
//        Book book = orderDetail.getBook();
//
//        int stock = book.getStock();
//        book.setStock(stock + orderDetail.getQuantity());
//
//        deliveryService.deliveryCheckForCancel(orderDetail.getOrder().getId());
//    }
}
