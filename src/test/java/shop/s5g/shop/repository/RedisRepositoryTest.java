package shop.s5g.shop.repository;

import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import shop.s5g.shop.repository.cart.CartRedisRepository;

@ExtendWith(SpringExtension.class)
@DisabledIf("#{T(org.springframework.util.StringUtils).hasText(environment['spring.profiles.active']) && environment['spring.profiles.active'].contains('disable-redis')}")
@Testcontainers(disabledWithoutDocker = true)
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
        String customerLoginId = "TestCustomerLoginId";
        cartRedisRepository.setLoginFlag(customerLoginId);

        Boolean isLoggedIn = (Boolean) redisTemplate.opsForValue()
            .get(CartRedisRepository.IS_LOGGED_IN + customerLoginId);

        Assertions.assertThat(isLoggedIn).isTrue();
    }

    @Test
    void getLoginFlagTest() {

        String customerLoginId = "TestCustomerLoginId";
        cartRedisRepository.setLoginFlag(customerLoginId);

        Assertions.assertThat(cartRedisRepository.getLoginFlag(customerLoginId)).isTrue();
    }

    @Test
    void deleteLoginFlagTest() {

        String sessionId = "testSessionId";
        cartRedisRepository.setLoginFlag(sessionId);

        cartRedisRepository.deleteLoginFlag(sessionId);

        Assertions.assertThat(redisTemplate.opsForValue()
            .get(CartRedisRepository.IS_LOGGED_IN + sessionId)).isEqualTo(null);
    }

    @Test
    void putBookTest() {
        Long bookId = 1l;
        Integer quantity = 1;
        String customerLoginId = "TestCustomerLoginId";


        cartRedisRepository.putBook(bookId, quantity, customerLoginId);

        Assertions.assertThat(
                redisTemplate.opsForHash().get(CartRedisRepository.CART + customerLoginId, bookId))
            .isEqualTo(1);
    }

    @Test
    void deleteOldCartTest() {
        Long bookId = 1l;
        Integer quantity = 1;
        String customerLoginId = "TestCustomerLoginId";


        cartRedisRepository.putBook(bookId, quantity, customerLoginId);

        cartRedisRepository.deleteOldCart(customerLoginId);

        Assertions.assertThat(
                redisTemplate.opsForHash().entries(CartRedisRepository.CART + customerLoginId))
            .isEmpty();
    }

    @Test
    void putBookByMapTest() {
        // given
        String customerLoginId = "TestCustomerLoginId";
        Map<Long, Integer> books = new HashMap<>(Map.of(1l, 1, 2l, 2));

        // when
        cartRedisRepository.putBookByMap(books, customerLoginId);

        // then
        Assertions.assertThat(
                redisTemplate.opsForHash().entries(CartRedisRepository.CART + customerLoginId))
            .isEqualTo(books);
    }


    @Test
    void getBooksInRedisCartTest()  {

        String customerLoginId = "TestCustomerLoginId";

        Map<Long, Integer> books = new HashMap<>(Map.of(1l, 1, 2l, 2));
        cartRedisRepository.putBookByMap(books, customerLoginId);

        Assertions.assertThat(
                cartRedisRepository.getBooksInRedisCart(customerLoginId))
            .isEqualTo(books);

    }

    @Test
    void reduceBookQuantityTest() {
        String customerLoginId = "TestCustomerLoginId";

        Map<Long, Integer> books = new HashMap<>(Map.of(1l, 1, 2l, 2));
        cartRedisRepository.putBookByMap(books, customerLoginId);
        Long bookId = 2l;

        cartRedisRepository.reduceBookQuantity(bookId, customerLoginId);
        Map<Long, Integer> ReducedBooks = new HashMap<>(Map.of(1l, 1, 2l, 1));

        Assertions.assertThat(
                redisTemplate.opsForHash().entries(CartRedisRepository.CART + customerLoginId))
            .isEqualTo(ReducedBooks);

    }

    @Test
    void reduceBookQuantityDeleteTest() {
        String customerLoginId = "TestCustomerLoginId";

        Map<Long, Integer> books = new HashMap<>(Map.of(1l, 1, 2l, 2));
        cartRedisRepository.putBookByMap(books, customerLoginId);


        Long bookId = 1l;
        Integer quantity = 1;
        cartRedisRepository.reduceBookQuantity(bookId, customerLoginId);

        Map<Long, Integer> ReducedBooks = new HashMap<>(Map.of(2l, 2));
        Assertions.assertThat(
                redisTemplate.opsForHash().entries(CartRedisRepository.CART + customerLoginId))
            .isEqualTo(ReducedBooks);

    }

    @Test
    void deleteBookFromCartTest() {
        String customerLoginId = "TestCustomerLoginId";

        Map<Long, Integer> books = new HashMap<>(Map.of(1l, 1, 2l, 2));
        cartRedisRepository.putBookByMap(books, customerLoginId);

        Long bookId = 2l;
        cartRedisRepository.deleteBookFromCart(bookId, customerLoginId);

        Map<Long, Object> deletedBooks = new HashMap<>(Map.of(1l, 1));
        Assertions.assertThat(
                redisTemplate.opsForHash().entries(CartRedisRepository.CART + customerLoginId))
            .isEqualTo(deletedBooks);

    }
}


