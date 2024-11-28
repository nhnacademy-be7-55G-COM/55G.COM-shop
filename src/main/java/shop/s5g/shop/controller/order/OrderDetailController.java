package shop.s5g.shop.controller.order;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.delivery.DeliveryResponseDto;
import shop.s5g.shop.dto.order.DetailStatusChangeRequestDto;
import shop.s5g.shop.dto.order.OrderDetailInfoDto;
import shop.s5g.shop.dto.order.OrderDetailWithBookResponseDto;
import shop.s5g.shop.dto.refund.RefundHistoryResponseDto;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.security.ShopMemberDetail;
import shop.s5g.shop.service.delivery.DeliveryService;
import shop.s5g.shop.service.order.OrderDetailService;
import shop.s5g.shop.service.order.OrderService;
import shop.s5g.shop.service.order.RefundHistoryService;

@RequiredArgsConstructor
@RequestMapping("/api/shop/orders")
@RestController
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    private final DeliveryService deliveryService;
    private final OrderService orderService;
    private final RefundHistoryService refundHistoryService;

    // 하나의 주문에 대한 주문 상세, 환불내역, 배송지 모두 리턴하는 컨트롤러
    @PostAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN') or returnObject.customerId == principal.getCustomerId())")
    @GetMapping("/{orderId}")
    public Object getOrderDetailAll(
        @AuthenticationPrincipal ShopMemberDetail memberDetail,
        @PathVariable long orderId, @RequestParam(required = false) String scope
    ) {
        long ownerId = orderService.getCustomerIdWithOrderId(orderId);
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
                orderDetails, delivery, refunds, ownerId
            );
        }
        throw new BadRequestException("다음 인자는 잘못된 인자입니다: scope="+scope);
    }

    @PutMapping("/details/{detailId}")
    public ResponseEntity<HttpStatus> changeDetailType(
        @AuthenticationPrincipal ShopMemberDetail memberDetail,
        @PathVariable long detailId, @RequestBody DetailStatusChangeRequestDto change
    ) {
        orderDetailService.changeOrderDetailType(detailId, change.status());
        return ResponseEntity.ok().build();
    }
}
