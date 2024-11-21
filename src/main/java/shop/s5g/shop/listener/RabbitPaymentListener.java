package shop.s5g.shop.listener;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.order.OrderRabbitResponseDto;
import shop.s5g.shop.dto.payments.TossPaymentsDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.service.order.OrderService;
import shop.s5g.shop.service.payments.AbstractPaymentManager;

@Slf4j(topic = "RABBITMQ_LISTENER")
@Service
public class RabbitPaymentListener {
    private final AbstractPaymentManager paymentManager;
    private final OrderService orderService;

    public RabbitPaymentListener(
        @Qualifier("tossPaymentsManager")
        AbstractPaymentManager paymentManager,
        OrderService orderService
    ) {
        this.paymentManager = paymentManager;
        this.orderService = orderService;
    }

    @RabbitListener(
        queues = "${rabbit.orders.payment.toss.queue}",
        concurrency = "1",
        executor = "rabbitExecutor", messageConverter = "jacksonMessageConverter"
    )
    public OrderRabbitResponseDto paymentListener(Map<String, Object> body){
        long orderDataId = ((Number) body.get("orderDataId")).longValue();
        long usedPoint = ((Number)body.get("usedPoint")).longValue();
        log.debug("Rabbit payment request just received for orderId={}", orderDataId);
        Map<String, Object> paymentInfo = extractPaymentInfo(body);
        try {
            paymentManager.confirmPayment(orderDataId, usedPoint, paymentInfo,
                TossPaymentsDto.class);
        } catch (RuntimeException e) {
            String rootMessage = ExceptionUtils.getRootCauseMessage(e);
            log.info("결제 과정 중 오류 발생: {}", rootMessage);
            orderService.deactivateOrder(orderDataId);
            return new OrderRabbitResponseDto(false, rootMessage);
        }

        return new OrderRabbitResponseDto(true, "confirmed");
    }

    @RabbitListener(
        queues = "${rabbit.orders.payment.toss.dlq}",
        concurrency = "2",
        executor = "rabbitExecutor", messageConverter = "jacksonMessageConverter"
    )
    public MessageDto paymentDeadMessageListener(Map<String, Object> body) {
        long orderDataId = ((Number) body.get("orderDataId")).longValue();
        log.error("Rabbit payment request was dead for orderId={}\n", orderDataId);
        // 결제에 실패했을경우, payment 레코드를 따로 만들지 않게 구분.
//        orderService.deactivateOrder(orderDataId);  // de-active.

        return new MessageDto("payment failed");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractPaymentInfo(Map<String, Object> body) {
        return (Map<String, Object>) body.get("payment");
    }
}
