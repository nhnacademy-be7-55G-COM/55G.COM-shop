package shop.S5G.shop.controller.order;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
import shop.S5G.shop.service.order.OrderService;

@RequiredArgsConstructor
@RequestMapping("/api/shop/orders")
@RestController
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderWithDetailResponseDto> queryAllOrders(@RequestParam long customerId, @RequestParam(required = false) OrderQueryRequestDto queryRequest) {
        if (queryRequest == null)
            return orderService.getAllOrdersWithDetail(customerId);
        else
            return orderService.getAllOrdersBetweenDates(queryRequest);
    }

    @PostMapping
    public ResponseEntity<OrderCreateResponseDto> createNewOrder(@Valid @RequestBody OrderCreateRequestDto requestDto, BindingResult result) {
        if (result.hasErrors()) {
            throw new BadRequestException("Order creation failed: bad request");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
            orderService.createOrder(requestDto)
        );
    }

}
