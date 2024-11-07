package shop.S5G.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.S5G.shop.dto.delivery.DeliveryResponseDto;
import shop.S5G.shop.dto.delivery.DeliveryUpdateRequestDto;
import shop.S5G.shop.service.order.DeliveryService;

@RequiredArgsConstructor
@RequestMapping("/api/shop/delivery")
@Controller
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PutMapping("/admin")
    public DeliveryResponseDto adminUpdateDelivery(@RequestBody DeliveryUpdateRequestDto updateRequest) {
        return deliveryService.adminUpdateDelivery(updateRequest);
    }

    @PutMapping
    public DeliveryResponseDto userUpdateDelivery(@RequestBody DeliveryUpdateRequestDto updateRequest) {
        return deliveryService.userUpdateDelivery(updateRequest);
    }
}
