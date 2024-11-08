package shop.S5G.shop.repository.cart;


import java.util.Map;
import java.util.Objects;
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


    public void setLoginFlag(String customerLoginId) {
        redisTemplate.opsForValue().set(IS_LOGGED_IN + customerLoginId, true);
    }


    public Boolean getLoginFlag(String customerLoginId) {
        return (Boolean) redisTemplate.opsForValue().get(IS_LOGGED_IN + customerLoginId);
    }

    public void deleteLoginFlag(String customerLoginId) {
        redisTemplate.delete(IS_LOGGED_IN + customerLoginId);
    }


    public Map<Object, Object> getBooksInRedisCart(String customerLoginId) {

        Map<Object, Object> booksInRedisCart = redisTemplate.opsForHash().entries(CART + customerLoginId);

        return booksInRedisCart;

    }

    public void deleteOldCart(String customerLoginId) {
        redisTemplate.delete(CART + customerLoginId);
    }

    public void putBookByMap(Map<Long,Integer> books,String customerLoginId) {
        redisTemplate.opsForHash().putAll(CART + customerLoginId, books);

    }

    public void putBook(Long bookId,Integer quantity,String customerLoginId) {
        Integer newQuantity = Optional.ofNullable(
                redisTemplate.opsForHash().get(CART + customerLoginId, bookId))
            .map(currentQuantity -> (Integer) currentQuantity + quantity).orElse(quantity);

        redisTemplate.opsForHash().put(CART + customerLoginId, bookId, newQuantity);
    }



    public void reduceBookQuantity(Long bookId, String customerLoginId) {
        Integer existingQuantity = (Integer) redisTemplate.opsForHash().get(CART + customerLoginId, bookId);

        if (Objects.nonNull(existingQuantity)){
            if (existingQuantity <= 1) {
                redisTemplate.opsForHash().delete(CART + customerLoginId, bookId);
                return;
            }

            redisTemplate.opsForHash().put(CART + customerLoginId, bookId, existingQuantity - 1);
        }

    }


    public void deleteBookFromCart(Long bookId, String customerLoginId) {
        redisTemplate.opsForHash().delete(CART + customerLoginId, bookId);
    }


}
