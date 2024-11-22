package shop.s5g.shop.listener;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.order.OrderRabbitResponseDto;
import shop.s5g.shop.dto.payments.TossPaymentsDto;
import shop.s5g.shop.exception.ResourceNotFoundException;
import shop.s5g.shop.exception.order.OrderDoesNotProceedException;
import shop.s5g.shop.service.order.OrderService;
import shop.s5g.shop.service.payments.AbstractPaymentManager;

@Slf4j(topic = "RABBITMQ_LISTENER")
@Service
public class RabbitPaymentListener {
    private final AbstractPaymentManager paymentManager;
    private final OrderService orderService;
    private final RabbitTemplate rabbitTemplate;
    private static final String X_COUNT_HEADER = "x-retry-count";

    @Value("${rabbit.retry-count}")
    private int retryCount;

    @Value("${rabbit.exchange.orders}")
    private String orderExchange;

    @Value("${rabbit.route.orders.payment.toss}")
    private String orderTossRouteKey;

    public RabbitPaymentListener(
        @Qualifier("tossPaymentsManager")
        AbstractPaymentManager paymentManager,
        OrderService orderService,
        RabbitTemplate rabbitTemplate
    ) {
        this.paymentManager = paymentManager;
        this.orderService = orderService;
        this.rabbitTemplate = rabbitTemplate;
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
        } catch (OrderDoesNotProceedException | ResourceNotFoundException e) {
            String rootMessage = ExceptionUtils.getRootCauseMessage(e);
            log.info("결제 과정 중 오류 발생: {}", rootMessage);
            orderService.deactivateOrder(orderDataId);

            return new OrderRabbitResponseDto(false, rootMessage);
        } catch (ClassCastException e) {
            log.error("Class cast failed, check dto", e);
            return new OrderRabbitResponseDto(false, e.getMessage());
        }

        return new OrderRabbitResponseDto(true, "confirmed");
    }

    @RabbitListener(
        queues = "${rabbit.orders.payment.toss.dlq}",
        concurrency = "1",
        executor = "rabbitExecutor"
    )
    public void paymentDeadMessageListener(Message failedMessage) {
        Integer count = (Integer) failedMessage.getMessageProperties().getHeaders().get(X_COUNT_HEADER);
        if (count == null) {
            count = 0;
        }
        if (count > retryCount) {
            log.warn("Message discarded: {}", new String(failedMessage.getBody()));
            return;
        }
        log.info("Message retrying count: {}", count);
        failedMessage.getMessageProperties().setHeader(X_COUNT_HEADER, count+1);
        rabbitTemplate.send(orderExchange, orderTossRouteKey, failedMessage);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> extractPaymentInfo(Map<String, Object> body) {
        return (Map<String, Object>) body.get("payment");
    }
}
