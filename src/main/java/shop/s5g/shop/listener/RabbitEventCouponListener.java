package shop.s5g.shop.listener;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.tag.MessageDto;
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
    public MessageDto giveEventCoupon(Long customerId) {

        return new MessageDto("Success");
    }

    @RabbitListener(
        queues = "${rabbit.coupon.event.create.dlq}",
        concurrency = "1",
        executor = "rabbitExecutor", messageConverter = "jacksonMessageConverter"
    )
    public MessageDto deadEventCoupon(Long customerId) {

        return new MessageDto("Failed");
    }

    @RabbitListener(
        queues = "${rabbit.category.event.coupon.create.queue}",
        concurrency = "1",
        executor = "rabbitExecutor", messageConverter = "jacksonMessageConverter"
    )
    public MessageDto receiveCategoryCoupon(Map<String, Object> body) {

//        String categoryName = (String) body.get("categoryName");
//        Long currentMemberId = ((Number) body.get("memberId")).longValue();
//
//        userCouponService.createCategoryCoupon(currentMemberId, categoryName);

        return new MessageDto("Success");
    }

    @RabbitListener(
        queues = "${rabbit.category.event.coupon.create.dlq}",
        concurrency = "1",
        executor = "rabbitExecutor", messageConverter = "jacksonMessageConverter"
    )
    public MessageDto deadReceiveCategoryCoupon(Long customerId) {

        return new MessageDto("Failed");
    }
}
