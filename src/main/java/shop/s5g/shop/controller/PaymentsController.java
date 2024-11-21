package shop.s5g.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    /**
     * {
     *     "detailId": number,
     *     "cancelInfo": {
     *         "cancelReason": "이런저런사유"
     *     }
     * }
     */

    @PutMapping("/cancel")
    public MessageDto cancelPayment(@RequestBody Map<String, Object> body) {
        long detailId = ((Number) body.get("detailId")).longValue();
        Map<String, Object> cancelInfo = extractCancelInfo(body);

        paymentManager.cancelPayment(detailId, cancelInfo, TossPaymentsDto.class);
        return new MessageDto("canceled");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractCancelInfo(Map<String, Object> body) {
        return (Map<String, Object>) body.get("cancelInfo");
    }
}
