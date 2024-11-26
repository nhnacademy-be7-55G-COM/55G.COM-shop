package shop.s5g.shop.listener;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.coupon.user.UserCouponRabbitResponseDto;
import shop.s5g.shop.service.coupon.user.UserCouponService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitEventCouponListener {

    private final UserCouponService userCouponService;

    @RabbitListener(
        queues = "${rabbit.coupon.event.create.queue}",
        concurrency = "1",
        executor = "rabbitExecutor", messageConverter = "jacksonMessageConverter"
    )
    public UserCouponRabbitResponseDto giveEventCoupon(Map<String, Object> body) {

        long memberId = ((Number) body.get("memberId")).longValue();
        long couponId = ((Number) body.get("couponId")).longValue();
        long couponTemplateId = ((Number) body.get("couponTemplateId")).longValue();

        log.debug("Rabbit Coupon request received for memberId = {} & couponId = {} & couponTemplateId = {}",
            memberId, couponId, couponTemplateId);

        return new UserCouponRabbitResponseDto(true, "Success");
    }

    @RabbitListener(
        queues = "${rabbit.coupon.event.create.dlq}",
        concurrency = "1",
        executor = "rabbitExecutor"
    )
    public void deadEventCoupon(Message failedMessage) {
    }
}
