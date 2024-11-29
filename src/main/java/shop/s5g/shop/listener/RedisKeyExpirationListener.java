package shop.s5g.shop.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import shop.s5g.shop.config.RedisConfig;
import shop.s5g.shop.service.cart.CartService;

@Slf4j
@ConditionalOnBean(RedisConfig.class)
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    private CartService cartService;

    @Autowired
    public RedisKeyExpirationListener(
        RedisMessageListenerContainer listenerContainer, CartService cartService) {
        super(listenerContainer);
        this.cartService = cartService;
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {

        if (message.toString().startsWith("refresh_token")) {

            String customerLoginId = message.toString().split(":")[1];
            try {
                cartService.FromRedisToDb(customerLoginId);
            } catch (Exception e) {
                log.debug("refresh_token 만료시 장바구니 db 저장에 실패했습니다.");
            }
        }
    }
}
