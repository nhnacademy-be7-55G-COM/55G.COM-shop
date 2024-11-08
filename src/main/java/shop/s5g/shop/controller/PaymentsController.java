package shop.s5g.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.order.OrderCreateRequestDto;
import shop.s5g.shop.dto.payments.TossPaymentsDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.security.ShopMemberDetail;
import shop.s5g.shop.service.payments.AbstractPaymentManager;

@RequestMapping("/api/shop/payment")
@RestController
@Slf4j
public class PaymentsController {
    private final AbstractPaymentManager paymentManager;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PaymentsController(@Qualifier("tossPaymentsManager") AbstractPaymentManager paymentManager) {
        this.paymentManager = paymentManager;
    }

    /*
     * {
     *   orderDataId: 1234,
     *   order: {
     *     ...
     *   },
     *   payment: {
     *     ...
     *   }
     * }
     */
    @PostMapping("/confirm")
    public MessageDto confirmPayment(
        @AuthenticationPrincipal ShopMemberDetail memberDetail,
        @RequestBody Map<String, Object> body
    ) {
        log.trace("Payment confirm request received: [customerId={}, loginId={}]", memberDetail.getCustomerId(), memberDetail.getLoginId());
//        log.trace("Payment confirm request for Order: {}", orderRelationId);
        long orderDataId = ((Number) body.get("orderDataId")).longValue();
        OrderCreateRequestDto order = extractOrderRequest(body);
        Map<String, Object> paymentInfo = extractPaymentInfo(body);

        paymentManager.confirmPayment(
            memberDetail.getCustomerId(),
            orderDataId,
            order,
            paymentInfo,
            TossPaymentsDto.class
        );
        // Not implemented yet.
        return new MessageDto("confirmed");
    }

    private OrderCreateRequestDto extractOrderRequest(Map<String, Object> body) {
        return objectMapper.convertValue(body.get("order"), OrderCreateRequestDto.class);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractPaymentInfo(Map<String, Object> body) {
        return (Map<String, Object>) body.get("payment");
    }
}
