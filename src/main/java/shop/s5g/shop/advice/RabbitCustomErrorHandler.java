package shop.s5g.shop.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.util.ErrorHandler;

@Slf4j
@RequiredArgsConstructor
public class RabbitCustomErrorHandler implements ErrorHandler {
    private final FatalExceptionStrategy exceptionStrategy;

    @Override
    public void handleError(Throwable t) {
        if (this.exceptionStrategy.isFatal(t) && t instanceof ListenerExecutionFailedException) {
            log.error("Rabbit Listener Fatal Exception: ",t);
            throw new ImmediateAcknowledgeAmqpException(
                "!Fatal Exception! Retry is unreasonable: " + t.getMessage(), t
            );
        }

        log.warn("Moving message to dead letter exchange: {}", t.getMessage());
        throw new AmqpRejectAndDontRequeueException(
            "Moving to DLX for retries: " + t.getMessage(), t
        );
    }
}
