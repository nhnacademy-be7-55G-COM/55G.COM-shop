package shop.S5G.shop.repository.cart;


import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import shop.S5G.shop.config.RedisConfig;

@Repository
@RequiredArgsConstructor
@ConditionalOnBean(RedisConfig.class)
public class CartRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    public static final String IS_LOGGED_IN = "isLoggedIn:";
    public static final String CART = "cart:";
    public static final String CUSTOMER_ID = "customerId:";

    public void setLoginFlag(String sessionId) {
        redisTemplate.opsForValue().set(IS_LOGGED_IN+sessionId, true);
    }

    public Boolean getLoginFlag(String sessionId) {
        return (Boolean) redisTemplate.opsForValue().get(IS_LOGGED_IN + sessionId);
    }

    public void deleteLoginFlag(String sessionId) {
        redisTemplate.delete(IS_LOGGED_IN + sessionId);
    }

    public void setCustomerId(String sessionId, String customerLoginId) {
        redisTemplate.opsForValue().set(CUSTOMER_ID + sessionId, customerLoginId);
    }

    public String getCustomerId(String sessionId) {
        return (String) redisTemplate.opsForValue().get(CUSTOMER_ID + sessionId);
    }

    public void deleteCustomerId(String sessionId) {
        redisTemplate.delete(CUSTOMER_ID + sessionId);
    }

    public Map<Object, Object> getBooksInRedisCart(String sessionId) {

        Map<Object, Object> booksInRedisCart = redisTemplate.opsForHash().entries(CART + sessionId);

        return booksInRedisCart;

    }

    public void deleteOldCart(String sessionId) {
        redisTemplate.delete(CART + sessionId);
    }


    public void putBook(Long bookId,Integer quantity,String sessionId) {
        Integer newQuantity = Optional.ofNullable(
                redisTemplate.opsForHash().get(CART + sessionId, bookId))
            .map(currentQuantity -> (Integer) currentQuantity + quantity).orElse(quantity);

        redisTemplate.opsForHash().put(CART + sessionId, bookId, newQuantity);
    }

    public void putBookByMap(Map<Long, Integer> books,String sessionId) {
        redisTemplate.opsForHash().putAll(CART + sessionId, books);
    }



    public void reduceBookQuantity(Long bookId, String sessionId) {
        Integer existingQuantity = (Integer) redisTemplate.opsForHash().get(CART + sessionId, bookId);
        if (existingQuantity <= 1) {
            redisTemplate.opsForHash().delete(CART + sessionId, bookId);
            return;
        }

        redisTemplate.opsForHash().put(CART + sessionId, bookId, existingQuantity - 1);
    }


    public void deleteBookFromCart(Long bookId, String sessionId) {
        redisTemplate.opsForHash().delete(CART + sessionId, bookId);
    }


}
