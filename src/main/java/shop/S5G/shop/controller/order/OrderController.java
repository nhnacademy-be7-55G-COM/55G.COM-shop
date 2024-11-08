package shop.S5G.shop.controller.order;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.dto.order.OrderCreateRequestDto;
import shop.S5G.shop.dto.order.OrderCreateResponseDto;
import shop.S5G.shop.dto.order.OrderQueryRequestDto;
import shop.S5G.shop.dto.order.OrderWithDetailResponseDto;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.security.ShopMemberDetail;
import shop.S5G.shop.service.order.OrderService;

@RequiredArgsConstructor
@RequestMapping("/api/shop/orders")
@RestController
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderWithDetailResponseDto> queryAllOrders(
        @AuthenticationPrincipal ShopMemberDetail memberDetail,
        @RequestParam(required = false) OrderQueryRequestDto queryRequest
    ) {
        if (queryRequest == null)
            return orderService.getAllOrdersWithDetail(memberDetail.getCustomerId());
        else
            return orderService.getAllOrdersBetweenDates(memberDetail.getCustomerId(), queryRequest);
    }

    @PostMapping
    public ResponseEntity<OrderCreateResponseDto> createNewOrder(
        @AuthenticationPrincipal ShopMemberDetail memberDetail,
        @Valid @RequestBody OrderCreateRequestDto requestDto,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            throw new BadRequestException("Order creation failed: bad request");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
            orderService.createOrder(memberDetail.getCustomerId(), requestDto)
        );
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable long orderId) {
        orderService.deactivateOrder(orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
