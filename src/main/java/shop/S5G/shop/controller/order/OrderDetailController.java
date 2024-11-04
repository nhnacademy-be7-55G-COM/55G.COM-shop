package shop.S5G.shop.controller.order;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.dto.delivery.DeliveryResponseDto;
import shop.S5G.shop.dto.order.OrderDetailInfoDto;
import shop.S5G.shop.dto.order.OrderDetailWithBookResponseDto;
import shop.S5G.shop.dto.refund.RefundHistoryResponseDto;
import shop.S5G.shop.repository.order.RefundImageRepository;
import shop.S5G.shop.service.order.DeliveryService;
import shop.S5G.shop.service.order.OrderDetailService;
import shop.S5G.shop.service.order.RefundHistoryService;

@RequiredArgsConstructor
@RequestMapping("/api/shop/orders/{orderId}")
@RestController
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    private final DeliveryService deliveryService;
    private final RefundHistoryService refundHistoryService;
    private final RefundImageRepository refundImageRepository;

    @GetMapping
    public List<OrderDetailWithBookResponseDto> getOrderDetails(@PathVariable long orderId) {
        return orderDetailService.findOrderDetailsByOrderId(orderId);
    }

    // 하나의 주문에 대한 주문 상세, 환불내역, 배송지 모두 리턴하는 컨트롤러
    @GetMapping
    public Object getOrderDetailAll(@PathVariable long orderId, @RequestParam String scope) {
        if (scope.equals("all")) {
            List<OrderDetailWithBookResponseDto> orderDetails = orderDetailService.findOrderDetailsByOrderId(
                orderId);
            DeliveryResponseDto delivery = deliveryService.findById(orderId);

            List<RefundHistoryResponseDto> refunds = orderDetails.stream().map(
                details -> refundHistoryService.getHistoryByOrderDetailId(details.orderDetailId())
            ).toList();

            // TODO: 나중에 이미지도 추가해야함..
            return new OrderDetailInfoDto(
                orderDetails, delivery, refunds
            );
        }
        return getOrderDetails(orderId);
    }
}
