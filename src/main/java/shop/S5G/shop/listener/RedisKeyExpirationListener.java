package shop.S5G.shop.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import shop.S5G.shop.config.RedisConfig;
import shop.S5G.shop.service.cart.CartService;


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
        if (message.toString().startsWith("spring:session:sessions")) {
            // session 이 만료되는 경우에만 시행
            String sessionId = message.toString().split(":")[3];

            if (cartService.getLoginFlag(sessionId) == true) {
                // 로그인 중에 세션 만료된 경우에만 아래 로직을 시행 (비로그인시 만료될 경우 동작하지 않도록 함)
                String customerLoginId = cartService.getCustomerId(sessionId);
                // 로그인할 때는 Db에 있는 것과 비로그인 Redis 를 합치면 됐지만 로그아웃될 때는 합치는 게 아니라 Redis 에 있는 걸로 Db가 초기화가 되어야된다.

                cartService.sessionExpirationInitializing(sessionId, customerLoginId);

            } else {
                // 비로그인시 카트는 삭제
                cartService.deleteOldCart(sessionId);
            }
        }
    }
}
