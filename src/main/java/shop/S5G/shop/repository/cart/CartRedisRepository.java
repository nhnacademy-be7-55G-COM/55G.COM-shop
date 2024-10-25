package shop.S5G.shop.repository.cart;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    public static final String IS_LOGGED_IN = "isLoggedIn:";
    public static final String CART = "cart:";
    public static final String CUSTOMER_ID = "customerId:";

    public void setLoginFlag(String sessionId) {
        redisTemplate.opsForValue().set(IS_LOGGED_IN+sessionId, true);
    }

    public void deleteLoginFlag(String sessionId) {
        redisTemplate.delete(IS_LOGGED_IN + sessionId);
    }

    public void setCustomerId(String sessionId, Long customerId) {
        redisTemplate.opsForValue().set(CUSTOMER_ID + sessionId, customerId);
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
        redisTemplate.opsForHash().put(CART + sessionId, bookId , quantity);
    }

    public void putBookByMap(Map<String, Object> books,String sessionId) {
        redisTemplate.opsForHash().putAll(CART + sessionId, books);
    }



    public void reduceBookQuantity(Long bookId, String sessionId, Integer quantity) {
        if ((Integer) redisTemplate.opsForHash().get(CART + sessionId, bookId) <= 1) {
            redisTemplate.opsForHash().delete(CART + sessionId, bookId);
            return;
        }

        redisTemplate.opsForHash().increment(CART + sessionId, bookId, -1);
    }


    public void deleteBookFromCart(Long bookId, String sessionId, Integer quantity) {
        redisTemplate.opsForHash().delete(CART + sessionId, bookId);
    }


}
