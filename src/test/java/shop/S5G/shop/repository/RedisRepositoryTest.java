package shop.S5G.shop.repository;



import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import shop.S5G.shop.repository.cart.CartRedisRepository;

@Testcontainers
class RedisRepositoryTest {

    private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
    private static final int REDIS_PORT = 6379;
    private static final GenericContainer REDIS_CONTAINER;


    static {
        REDIS_CONTAINER = new GenericContainer(REDIS_IMAGE)
            .withExposedPorts(REDIS_PORT)
            .withReuse(true);
        REDIS_CONTAINER.start();
    }

    private static CartRedisRepository cartRedisRepository;
    private static RedisTemplate<String, Object> redisTemplate;

    @BeforeAll
    static void setUp() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory(REDIS_CONTAINER.getHost(),
            REDIS_CONTAINER.getMappedPort(REDIS_PORT));
        factory.start();
        factory.afterPropertiesSet();

        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.afterPropertiesSet();

        cartRedisRepository = new CartRedisRepository(redisTemplate);
    }

    @BeforeEach
    void flushRedis() {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.serverCommands().flushDb();
            return null;
        });
    }


    @Test
    void setLoginFlagTest() {
        String sessionId = "testSessionId";
        cartRedisRepository.setLoginFlag(sessionId);

        Boolean isLoggedIn = (Boolean) redisTemplate.opsForValue()
            .get(CartRedisRepository.IS_LOGGED_IN + sessionId);

        Assertions.assertThat(isLoggedIn).isTrue();
    }

    @Test
    @Order(2)
    void deleteLoginFlagTest() {

        String sessionId = "testSessionId";
        cartRedisRepository.setLoginFlag(sessionId);

        cartRedisRepository.deleteLoginFlag(sessionId);

        Assertions.assertThat(redisTemplate.opsForValue()
            .get(CartRedisRepository.IS_LOGGED_IN + sessionId)).isEqualTo(null);
    }

    @Test
    void setCustomerIdTest() {
        String sessionId = "testSessionId";
        Long customerId = 1l;

        cartRedisRepository.setCustomerId(sessionId, customerId);

        Assertions.assertThat(
                redisTemplate.opsForValue().get(CartRedisRepository.CUSTOMER_ID + sessionId))
            .isEqualTo(1l);

    }

    @Test
    void deleteCustomerIdTest() {

        String sessionId = "testSessionId";
        Long customerId = 1l;
        cartRedisRepository.setCustomerId(sessionId, customerId);

        cartRedisRepository.deleteCustomerId(sessionId);

        Assertions.assertThat(redisTemplate.opsForValue()
            .get(CartRedisRepository.IS_LOGGED_IN + sessionId)).isEqualTo(null);
    }


    @Test
    void putBookTest() {
        Long bookId = 1l;
        Integer quantity = 1;
        String sessionId = "testSessionId";

        cartRedisRepository.putBook(bookId, quantity, sessionId);

        Assertions.assertThat(
                redisTemplate.opsForHash().get(CartRedisRepository.CART + sessionId, bookId))
            .isEqualTo(1);
    }

    @Test
    void deleteOldCartTest() {
        Long bookId = 1l;
        Integer quantity = 1;
        String sessionId = "testSessionId";

        cartRedisRepository.putBook(bookId, quantity, sessionId);

        cartRedisRepository.deleteOldCart(sessionId);

        Assertions.assertThat(redisTemplate.opsForHash().entries(sessionId)).isEmpty();
    }

    @Test
    void putBookByMapTest() {

        String sessionId = "testSessionId";
        Map<Long, Object> books = new HashMap<>(Map.of(1l, 1, 2l, 2));

        cartRedisRepository.putBookByMap(books, sessionId);

        Assertions.assertThat(
                redisTemplate.opsForHash().entries(CartRedisRepository.CART + sessionId))
            .isEqualTo(books);

    }

    @Test
    void getBooksInRedisCartTest()  {


        String sessionId = "testSessionId";
        Map<Long, Object> books = new HashMap<>(Map.of(1l, 1, 2l, 2));
        cartRedisRepository.putBookByMap(books, sessionId);

        Assertions.assertThat(
                cartRedisRepository.getBooksInRedisCart(sessionId))
            .isEqualTo(books);

    }

    @Test
    void reduceBookQuantityTest() {
        String sessionId = "testSessionId";
        Map<Long, Object> books = new HashMap<>(Map.of(1l, 1, 2l, 2));
        cartRedisRepository.putBookByMap(books, sessionId);


        Long bookId = 2l;
        Integer quantity = 1;
        cartRedisRepository.reduceBookQuantity(bookId, sessionId, quantity);


        Map<Long, Object> ReducedBooks = new HashMap<>(Map.of(1l, 1, 2l, 1));
        Assertions.assertThat(
                redisTemplate.opsForHash().entries(CartRedisRepository.CART + sessionId))
            .isEqualTo(ReducedBooks);

    }

    @Test
    void reduceBookQuantityDeleteTest() {
        String sessionId = "testSessionId";
        Map<Long, Object> books = new HashMap<>(Map.of(1l, 1, 2l, 2));
        cartRedisRepository.putBookByMap(books, sessionId);


        Long bookId = 1l;
        Integer quantity = 1;
        cartRedisRepository.reduceBookQuantity(bookId, sessionId, quantity);

        Map<Long, Object> ReducedBooks = new HashMap<>(Map.of(2l, 2));
        Assertions.assertThat(
                redisTemplate.opsForHash().entries(CartRedisRepository.CART + sessionId))
            .isEqualTo(ReducedBooks);

    }

    @Test
    void deleteBookFromCartTest() {
        String sessionId = "testSessionId";

        Map<Long, Object> books = new HashMap<>(Map.of(1l, 1, 2l, 2));
        cartRedisRepository.putBookByMap(books, sessionId);

        Long bookId = 2l;
        cartRedisRepository.deleteBookFromCart(bookId, sessionId);

        Map<Long, Object> deletedBooks = new HashMap<>(Map.of(1l, 1));
        Assertions.assertThat(
                redisTemplate.opsForHash().entries(CartRedisRepository.CART + sessionId))
            .isEqualTo(deletedBooks);

    }
}
