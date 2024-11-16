package shop.s5g.shop.repository.cart;


import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import shop.s5g.shop.config.RedisConfig;
import shop.s5g.shop.dto.cart.request.CartBookSelectRequestDto;

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
    //TODO 수정필요
    public void putBookByMap(Map<Long,Integer> books,String customerLoginId) {
        redisTemplate.opsForHash().putAll(CART + customerLoginId, books);

    }

    public void putBook(Long bookId,Integer quantity,String customerLoginId) {

        CartFieldValue quantityStatus = (CartFieldValue) redisTemplate.opsForHash()
            .get(CART + customerLoginId, bookId);

        if (Objects.isNull(quantityStatus)) {
            redisTemplate.opsForHash()
                .put(CART + customerLoginId, bookId, new CartFieldValue(quantity, true));

        }else {
            quantityStatus.setQuantity(quantityStatus.getQuantity() + quantity);

            redisTemplate.opsForHash().put(CART + customerLoginId, bookId, quantityStatus);
        }


    }




    public void reduceBookQuantity(Long bookId, String customerLoginId) {
        CartFieldValue bookQuantityStatus = (CartFieldValue) redisTemplate.opsForHash().get(CART + customerLoginId, bookId);

        if (Objects.nonNull(bookQuantityStatus)){
            if (bookQuantityStatus.getQuantity() <= 1) {
                redisTemplate.opsForHash().delete(CART + customerLoginId, bookId);
                return;
            }

            bookQuantityStatus.setQuantity(bookQuantityStatus.getQuantity() - 1);
            redisTemplate.opsForHash().put(CART + customerLoginId, bookId, bookQuantityStatus);

        }

    }


    public void deleteBookFromCart(Long bookId, String customerLoginId) {
        redisTemplate.opsForHash().delete(CART + customerLoginId, bookId);
    }

    public void changeBookStatus(String customerLoginId,
        CartBookSelectRequestDto cartBookSelectRequestDto) {

        Optional.ofNullable(redisTemplate.opsForHash().get(CART + customerLoginId, cartBookSelectRequestDto.bookId())).ifPresent(book ->{
            CartFieldValue cartFieldValue = (CartFieldValue) book;
            cartFieldValue.setStatus(cartBookSelectRequestDto.status());
            redisTemplate.opsForHash()
                .put(CART + customerLoginId, cartBookSelectRequestDto.bookId(), cartFieldValue);
        });

    }
}
