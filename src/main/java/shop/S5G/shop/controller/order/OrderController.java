package shop.S5G.shop.controller.order;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.dto.order.OrderDetailWithBookResponseDto;
import shop.S5G.shop.dto.order.OrderWithDetailResponseDto;
import shop.S5G.shop.service.order.OrderService;

@RequiredArgsConstructor
@RequestMapping("/api/shop/orders")
@RestController
public class OrderController {
    private final OrderService orderService;

//    @GetMapping
//    public PageResponseDto<OrderWithDetailResponseDto> queryAllOrders(@RequestParam long customerId, Pageable pageable) {
//        return PageResponseDto.of(orderService.queryAllOrdersByCustomerId(customerId, pageable));
//    }
    @GetMapping
    public List<OrderWithDetailResponseDto> queryAllOrders(@RequestParam long customerId) {
        return orderService.queryAllOrdersByCustomerId(customerId);
    }

    @GetMapping("/details")
    public List<OrderDetailWithBookResponseDto> getOrderDetails(@RequestParam long orderId) {
        // TODO: Not Implemented yet.
        return List.of();
    }
}
