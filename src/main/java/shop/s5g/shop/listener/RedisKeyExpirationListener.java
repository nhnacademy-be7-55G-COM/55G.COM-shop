package shop.s5g.shop.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import shop.s5g.shop.config.RedisConfig;
import shop.s5g.shop.service.cart.CartService;


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

    }
}