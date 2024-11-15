package shop.s5g.shop.controller.delivery;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.s5g.shop.dto.delivery.DeliveryResponseDto;
import shop.s5g.shop.dto.delivery.DeliveryUpdateRequestDto;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.service.delivery.DeliveryService;

@RequiredArgsConstructor
@RequestMapping("/api/shop/delivery")
@Controller
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final BadRequestException ex = new BadRequestException("업데이트 형식이 맞지 않습니다");

    @PutMapping("/admin")
    public DeliveryResponseDto adminUpdateDelivery(
        @Valid @RequestBody DeliveryUpdateRequestDto updateRequest,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            throw ex;
        }
        return deliveryService.adminUpdateDelivery(updateRequest);
    }

    @PutMapping
    public DeliveryResponseDto userUpdateDelivery(
        @Valid @RequestBody DeliveryUpdateRequestDto updateRequest,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            throw ex;
        }
        return deliveryService.userUpdateDelivery(updateRequest);
    }
}
