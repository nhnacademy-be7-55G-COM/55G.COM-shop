package shop.s5g.shop.advice;

import java.util.LinkedList;
import java.util.List;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler.DefaultExceptionStrategy;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import shop.s5g.shop.exception.EssentialDataNotFoundException;
import shop.s5g.shop.exception.ResourceNotFoundException;
import shop.s5g.shop.exception.order.OrderDoesNotProceedException;

public class RabbitCustomExceptionStrategy implements FatalExceptionStrategy {
    private final FatalExceptionStrategy fatalExceptionStrategy = new DefaultExceptionStrategy();
    private final List<Class<? extends Throwable>> customFatalExceptions = new LinkedList<>();

    public RabbitCustomExceptionStrategy() {
        // 너무 치명적이어서 다시 시도해도 의미없는 예외들.
        customFatalExceptions.add(EssentialDataNotFoundException.class);
        customFatalExceptions.add(OrderDoesNotProceedException.class);
        customFatalExceptions.add(ResourceNotFoundException.class);
    }

    @Override
    public boolean isFatal(Throwable e) {
        // ListenerExecutionFailedException 으로 한번 포장됨.
        Class<? extends Throwable> expClass = e.getCause().getClass();
        return fatalExceptionStrategy.isFatal(e) || customFatalExceptions.stream().anyMatch(clazz -> clazz.isAssignableFrom(expClass));
    }
}
