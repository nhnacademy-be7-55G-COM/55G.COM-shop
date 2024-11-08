package shop.S5G.shop.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.dto.payments.TossPaymentsConfirmRequestDto;
import shop.S5G.shop.dto.tag.MessageDto;
import shop.S5G.shop.security.ShopMemberDetail;
import shop.S5G.shop.service.payments.TossPaymentsAdapterCaller;

@RequiredArgsConstructor
@RequestMapping("/api/shop/payment")
@RestController
@Slf4j
public class PaymentsController {
    private final TossPaymentsAdapterCaller paymentsCaller;

    @PostMapping("/confirm")
    public MessageDto confirmPayment(
        @AuthenticationPrincipal ShopMemberDetail memberDetail,
        @RequestBody Map<String, String> payment
    ) {
//        log.trace("Payment confirm request for Order: {}", orderRelationId);
        paymentsCaller.confirmPayment(TossPaymentsConfirmRequestDto.of(payment));
        // Not implemented yet.
        return new MessageDto("confirmed");
    }
}
