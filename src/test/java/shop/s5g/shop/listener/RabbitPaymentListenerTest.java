package shop.s5g.shop.listener;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.s5g.shop.dto.order.OrderRabbitResponseDto;
import shop.s5g.shop.exception.ResourceNotFoundException;
import shop.s5g.shop.exception.order.OrderDoesNotProceedException;
import shop.s5g.shop.service.order.OrderService;
import shop.s5g.shop.service.payments.AbstractPaymentManager;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {"rabbit.retry-count=5"})
@ContextConfiguration(classes = RabbitPaymentListener.class)
class RabbitPaymentListenerTest {
    @MockBean(name = "tossPaymentsManager")
    AbstractPaymentManager paymentManager;
    @MockBean
    OrderService orderService;
    @MockBean
    RabbitTemplate rabbitTemplate;
    @SpyBean
    ObjectMapper objectMapper;

    ObjectMapper jacksonConverter = new ObjectMapper();

    @Autowired
    RabbitPaymentListener listener;

    String req = """
        {
            "orderDataId": 1,
            "usedPoint": 0,
            "payment": {
                "paymentKey": "abcdefg"
            }
        }
        """;

    TypeReference<Map<String, Object>> typeReference = new TypeReference<Map<String, Object>>() {};

    Map<String, Object> correctRequest;
    Map<String, Object> payment = new HashMap<>();

    @BeforeEach
    void initRequests() throws Exception{
        correctRequest = jacksonConverter.readValue(req, typeReference);
    }

    @Test
    void paymentListenerTest() {
        assertThat(listener.paymentListener(correctRequest))
            .isInstanceOf(OrderRabbitResponseDto.class)
            .hasFieldOrPropertyWithValue("wellOrdered", true);

        verify(paymentManager, times(1)).confirmPayment(anyLong(), anyLong(), anyMap(), any());
        verify(orderService, never()).deactivateOrder(anyLong());
    }

    @Test
    void paymentListenerTest_ConfirmFailure2() {
        when(paymentManager.confirmPayment(anyLong(), anyLong(), anyMap(), any())).thenThrow(
            OrderDoesNotProceedException.class);

        assertThat(listener.paymentListener(correctRequest))
            .isInstanceOf(OrderRabbitResponseDto.class)
            .hasFieldOrPropertyWithValue("wellOrdered", false);

        verify(paymentManager, times(1)).confirmPayment(anyLong(), anyLong(), anyMap(), any());
        verify(orderService, times(1)).deactivateOrder(anyLong());
    }

    @Test
    void paymentListenerTest_ConfirmFailure1() {
        when(paymentManager.confirmPayment(anyLong(), anyLong(), anyMap(), any())).thenThrow(
            ResourceNotFoundException.class);

        assertThat(listener.paymentListener(correctRequest))
            .isInstanceOf(OrderRabbitResponseDto.class)
            .hasFieldOrPropertyWithValue("wellOrdered", false);

        verify(paymentManager, times(1)).confirmPayment(anyLong(), anyLong(), anyMap(), any());
        verify(orderService, times(1)).deactivateOrder(anyLong());
    }

    @Test
    void paymentDeadMessageListenerTest() {
        Message mockMessage = new Message(req.getBytes(StandardCharsets.UTF_8));

        assertThatCode(() -> listener.paymentDeadMessageListener(mockMessage))
            .doesNotThrowAnyException();

        verify(rabbitTemplate, times(1)).send(anyString(), anyString(), any(Message.class));
        verify(orderService, never()).deactivateOrder(anyLong());
    }

    @Test
    void paymentDeadMessageListenerTest_OutOfCount() {
        Message mockMessage = new Message(req.getBytes(StandardCharsets.UTF_8));
        mockMessage.getMessageProperties().setHeader("x-retry-count", 6);

        assertThatCode(() -> listener.paymentDeadMessageListener(mockMessage))
            .doesNotThrowAnyException();

        verify(rabbitTemplate, never()).send(anyString(), anyString(), any(Message.class));
        verify(orderService, times(1)).deactivateOrder(anyLong());
    }
}
