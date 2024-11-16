package shop.s5g.shop.listener;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.payments.TossPaymentsDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.service.payments.AbstractPaymentManager;

@Slf4j(topic = "RABBITMQ_LISTENER")
@Service
public class TossPaymentsRequestListener {
    private final AbstractPaymentManager paymentManager;

    public TossPaymentsRequestListener(@Qualifier("tossPaymentsManager") AbstractPaymentManager paymentManager) {
        this.paymentManager = paymentManager;
    }

    @RabbitListener(
        queues = "${rabbit.queue.orders.payment.toss}",
        executor = "rabbitExecutor", messageConverter = "jacksonMessageConverter"
    )
    public MessageDto paymentListener(Map<String, Object> body){
        long orderDataId = ((Number) body.get("orderDataId")).longValue();
        log.debug("Rabbit payment request received for orderId={}", orderDataId);
        Map<String, Object> paymentInfo = extractPaymentInfo(body);
        paymentManager.confirmPayment(0L, orderDataId, paymentInfo, TossPaymentsDto.class);

        return new MessageDto("confirmed");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractPaymentInfo(Map<String, Object> body) {
        return (Map<String, Object>) body.get("payment");
    }
}
