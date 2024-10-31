package shop.S5G.shop.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.dto.payments.TossPaymentsConfirmRequestDto;
import shop.S5G.shop.dto.tag.MessageDto;
import shop.S5G.shop.service.payments.TossPaymentsAdapterCaller;

@RequiredArgsConstructor
@RequestMapping("/api/shop/payment")
@RestController
@Slf4j
public class PaymentsController {
    private final TossPaymentsAdapterCaller paymentsCaller;

    @PostMapping("/confirm")
    public MessageDto confirmPayment(@RequestParam long orderRelationId, @RequestBody Map<String, String> payment) {
        log.trace("Payment confirm request for Order: {}", orderRelationId);
        paymentsCaller.confirmPayment(orderRelationId, TossPaymentsConfirmRequestDto.of(payment));
        // Not implemented yet.
        return new MessageDto("confirmed");
    }
}
