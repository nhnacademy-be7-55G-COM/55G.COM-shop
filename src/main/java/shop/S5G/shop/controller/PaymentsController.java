package shop.S5G.shop.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.dto.tag.MessageDto;
import shop.S5G.shop.service.payments.TossPaymentsService;

@RequiredArgsConstructor
@RequestMapping("/api/shop/payment")
@RestController
public class PaymentsController {
    private final TossPaymentsService paymentsService;

    @PostMapping("/confirm")
    public MessageDto confirmPayment(@RequestBody Map<String, Object> payment) {
        // Not implemented yet.
        return new MessageDto("confirmed");
    }
}
