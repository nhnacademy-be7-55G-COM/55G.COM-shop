package shop.s5g.shop.controller.order;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
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
import shop.s5g.shop.dto.order.OrderAdminTableView;
import shop.s5g.shop.dto.order.OrderCreateRequestDto;
import shop.s5g.shop.dto.order.OrderCreateResponseDto;
import shop.s5g.shop.dto.order.OrderQueryFilterDto;
import shop.s5g.shop.dto.order.OrderQueryRequestDto;
import shop.s5g.shop.dto.order.OrderWithDetailResponseDto;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.security.ShopMemberDetail;
import shop.s5g.shop.service.order.OrderService;

@RequiredArgsConstructor
@RequestMapping("/api/shop/orders")
@RestController
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderWithDetailResponseDto> queryAllOrders(
        @AuthenticationPrincipal ShopMemberDetail memberDetail,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
    ) {
        if (startDate == null || endDate == null)
            return orderService.getAllOrdersWithDetail(memberDetail.getCustomerId());
        else {
            return orderService.getAllOrdersBetweenDates(memberDetail.getCustomerId(),
                new OrderQueryRequestDto(startDate, endDate));
        }
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

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public List<OrderAdminTableView> fetchOrdersForAdmin(
        @Valid OrderQueryFilterDto filter,
        BindingResult errors
    ) {
        if (errors.hasErrors()) {
            throw new BadRequestException("필터가 잘못되었습니다");
        }
        return orderService.getOrderListAdmin(filter);
    }
}
