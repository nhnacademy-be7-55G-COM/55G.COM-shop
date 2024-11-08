package shop.s5g.shop.controller.order;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.delivery.DeliveryResponseDto;
import shop.s5g.shop.dto.order.OrderDetailInfoDto;
import shop.s5g.shop.dto.order.OrderDetailWithBookResponseDto;
import shop.s5g.shop.dto.refund.RefundHistoryResponseDto;
import shop.s5g.shop.service.delivery.DeliveryService;
import shop.s5g.shop.service.order.OrderDetailService;
import shop.s5g.shop.service.order.RefundHistoryService;

@RequiredArgsConstructor
@RequestMapping("/api/shop/orders/{orderId}")
@RestController
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    private final DeliveryService deliveryService;
    private final RefundHistoryService refundHistoryService;
//    private final RefundImageRepository refundImageRepository;

    // 하나의 주문에 대한 주문 상세, 환불내역, 배송지 모두 리턴하는 컨트롤러
    @GetMapping
    public Object getOrderDetailAll(@PathVariable long orderId, @RequestParam(required = false) String scope) {
        if (scope == null || scope.isEmpty()) {
            return orderDetailService.getOrderDetailsWithBook(orderId);
        } else if (scope.equals("all")) {
            List<OrderDetailWithBookResponseDto> orderDetails = orderDetailService.getOrderDetailsWithBook(
                orderId);
            DeliveryResponseDto delivery = deliveryService.getDelivery(orderId);

            List<RefundHistoryResponseDto> refunds = orderDetails.stream().map(
                details -> refundHistoryService.getRefundHistory(details.orderDetailId())
            ).toList();

            // TODO: 나중에 이미지도 추가해야함..
            return new OrderDetailInfoDto(
                orderDetails, delivery, refunds
            );
        }
        // TODO: 적절한 예외로 바꾸기
        throw new IllegalArgumentException();
    }
}
