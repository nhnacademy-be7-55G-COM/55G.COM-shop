package shop.s5g.shop.listener;

import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.coupon.user.UserCouponRabbitResponseDto;
import shop.s5g.shop.exception.coupon.CouponAlreadyIssuedException;
import shop.s5g.shop.exception.coupon.CouponNotFoundException;
import shop.s5g.shop.service.coupon.user.UserCouponService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitEventCouponListener {

    private final UserCouponService userCouponService;
    private final RabbitTemplate rabbitTemplate;
    private static final String X_COUNT_HEADER = "x-retry-count";

    @Value("${rabbit.retry-count}")
    private int retryCnt;

    @Value("${rabbit.exchange.coupons}")
    private String couponExchange;

    @Value("${rabbit.route.coupons.event}")
    private String couponRoutingKey;

    @RabbitListener(
        queues = "${rabbit.coupon.event.create.queue}",
        concurrency = "1",
        executor = "rabbitExecutor", messageConverter = "jacksonMessageConverter"
    )
    public UserCouponRabbitResponseDto giveEventCoupon(Map<String, Object> body) {

        long memberId = ((Number) body.get("memberId")).longValue();
        long couponTemplateId = ((Number) body.get("couponTemplateId")).longValue();

        log.debug("Rabbit Coupon request received for memberId = {} & couponTemplateId = {}",
            memberId, couponTemplateId);

        try {
            userCouponService.addUserCoupon(memberId, couponTemplateId);

        } catch (CouponNotFoundException e) {
            log.error("The coupon for the template has been issued : (TemplateId : {})", couponTemplateId, e);
            return new UserCouponRabbitResponseDto(false, "Failed");
        } catch (CouponAlreadyIssuedException e) {
            log.error("MemberId = {} already issued coupon for couponTemplateId = {}. Operation skipped.",
                memberId, couponTemplateId, e);
            return new UserCouponRabbitResponseDto(false, "Failed");
        } catch (Exception e) {
            log.error("Unexpected error occurred while processing RabbitMQ message for memberId = {} & couponTemplateId = {}.",
                memberId, couponTemplateId, e);
            return new UserCouponRabbitResponseDto(false, "Failed");
        }

        return new UserCouponRabbitResponseDto(true, "Message Success");
    }

    @RabbitListener(
        queues = "${rabbit.coupon.event.create.dlq}",
        concurrency = "1",
        executor = "rabbitExecutor"
    )
    public void deadEventCoupon(Message failedMessage) {
        Integer currentRetryCnt = (Integer) failedMessage.getMessageProperties().getHeaders().get(X_COUNT_HEADER);

        if (Objects.isNull(currentRetryCnt)) {
            currentRetryCnt = 0;
        }

        if (currentRetryCnt > retryCnt) {
            log.warn("Coupon Rabbit Message Queue discarded : {}", new String(failedMessage.getBody()));
            return;
        }

        log.info("Coupon Rabbit Message retrying count : {}", currentRetryCnt);
        failedMessage.getMessageProperties().setHeader(X_COUNT_HEADER, currentRetryCnt + 1);
        rabbitTemplate.send(couponExchange, couponRoutingKey, failedMessage);
    }
}
