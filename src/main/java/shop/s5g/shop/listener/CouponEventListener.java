package shop.s5g.shop.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import shop.s5g.shop.config.RedisConfig;
import shop.s5g.shop.dto.coupon.MemberRegisteredEvent;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.service.coupon.user.UserCouponService;

@Component
@RequiredArgsConstructor
@ConditionalOnBean(RedisConfig.class)
public class CouponEventListener {

    private final UserCouponService userCouponService;

    @EventListener
    public void handleMemberRegisterEvent(MemberRegisteredEvent event) {

        Member member = event.getMember();

        userCouponService.createWelcomeCoupon(member);
    }
}
