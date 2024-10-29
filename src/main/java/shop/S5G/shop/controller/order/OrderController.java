package shop.S5G.shop.controller.order;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.dto.order.OrderDetailWithBookResponseDto;
import shop.S5G.shop.dto.order.OrderWithDetailResponseDto;
import shop.S5G.shop.service.order.OrderDetailService;
import shop.S5G.shop.service.order.OrderService;

@RequiredArgsConstructor
@RequestMapping("/api/shop/orders")
@RestController
public class OrderController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @GetMapping
    public List<OrderWithDetailResponseDto> queryAllOrders(@RequestParam long customerId) {
        return orderService.queryAllOrdersByCustomerId(customerId);
    }

    @GetMapping("/{orderId}")
    public List<OrderDetailWithBookResponseDto> getOrderDetails(@PathVariable long orderId) {
        return orderDetailService.findOrderDetailsByOrderId(orderId);
    }
}
