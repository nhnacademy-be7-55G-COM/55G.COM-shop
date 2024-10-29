package shop.S5G.shop.service.cart.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import shop.S5G.shop.config.RedisConfig;
import shop.S5G.shop.dto.cart.response.CartBooksResponseDto;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.entity.cart.Cart;
import shop.S5G.shop.entity.cart.CartPk;
import shop.S5G.shop.entity.member.Member;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.repository.cart.CartRedisRepository;
import shop.S5G.shop.repository.cart.CartRepository;
import shop.S5G.shop.service.BookService;
import shop.S5G.shop.service.cart.CartService;
import shop.S5G.shop.service.member.MemberService;

@Service
@RequiredArgsConstructor
@ConditionalOnBean(RedisConfig.class)
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartRedisRepository cartRedisRepository;
    private final BookService bookService;
    private final MemberService memberService;

//     ----------- MySql && Redis 관련 -----------

    // 회원아이디를 이용해 Mysql 에 저장되어있는 Cart 리스트반환
    @Override
    public List<Cart> getBooksInDbByCustomerId(String customerLoginId) {
        Member member = memberService.findMember(customerLoginId);
        Long customerId = member.getId();

        return cartRepository.findAllByCartPk_CustomerId(customerId);
    }

    // Redis 와 Mysql 에 저장되어있는 걸 합쳐서 Db 에 저장
    @Override
    public void saveMergedCartToDb(String sessionId,String customerLoginId) {
        Member member = memberService.findMember(customerLoginId);

        Map<Long, Integer> booksInRedisCart = getBooksInRedisCart(sessionId);
        List<Cart> booksInDbCart = getBooksInDbByCustomerId(customerLoginId);


        // db 장바구니에 이미 같은 도서가 있는 경우는 개수를 합쳐준다.
        booksInDbCart.stream().forEach(cart -> cart.setQuantity(
            cart.getQuantity() + booksInRedisCart.getOrDefault(cart.getBook().getBookId(), 0)));

        // db 장바구니에 같은 도서가 없는 경우에는 추가해준다.
        booksInRedisCart.forEach((bookId,quantity) ->{
            boolean isNotInDb = (booksInDbCart.stream()
                .noneMatch(cart -> cart.getBook().getBookId().equals(bookId)));

            if (isNotInDb) {
                // Cart 객체를 생성하고 List 에 추가
                Book book = bookService.getBookById(bookId);
                Cart cart = new Cart(new CartPk(member.getId(), bookId), book, member, quantity);

                booksInDbCart.add(cart);
            }
        });
        deleteOldCart(sessionId);
        saveAll(booksInDbCart);
    }

    // Db 에 있는 걸 Redis 로 복사해준다.
    @Override
    public void transferCartFromDbToRedis(String sessionId, String customerLoginId) {

        setLoginFlag(sessionId);
        setCustomerId(sessionId, customerLoginId);

        List<Cart> booksInDbCart = getBooksInDbByCustomerId(customerLoginId);

        Map<Long, Integer> fromDbToRedis = new HashMap<>();

        booksInDbCart.stream()
            .forEach(cart -> fromDbToRedis.put(cart.getBook().getBookId(), cart.getQuantity()));

        putBookByMap(fromDbToRedis, sessionId);

    }

    @Override
    public void saveAll(List<Cart> mergedCart) {
        cartRepository.saveAll(mergedCart);
    }


    @Override
    public void sessionExpirationInitializing(String sessionId, String customerLoginId) {
        saveRedisToDb(sessionId, customerLoginId);

        deleteOldCart(sessionId);
        deleteCustomerId(sessionId);
        deleteLoginFlag(sessionId);
    }

    // 현재 회원 Db 장바구니를 비우고 redis 에 있는 걸 Db 에 채운다
    @Override
    public void saveRedisToDb(String sessionId, String customerLoginId) {
        List<Cart> redisToDb = new ArrayList<>();
        Member member = memberService.findMember(customerLoginId);
        deleteAllFromDbCartByCustomerId(member.getId());

        Map<Long, Integer> booksInRedisCart = getBooksInRedisCart(sessionId);

        booksInRedisCart.forEach((bookId,quantity) ->{
            Book book = bookService.getBookById(bookId);
            Cart cart = new Cart(new CartPk(member.getId(), bookId), book, member, quantity);

            redisToDb.add(cart);
        });

        saveAll(redisToDb);
    }

    @Override
    public void deleteAllFromDbCartByCustomerId(Long customerId) {
        cartRepository.deleteAllByCartPk_CustomerId(customerId);
    }



    // ----------- only Redis 관련 -----------


    @Override
    public void putBook(Long bookId, Integer quantity, String sessionId) {

        bookService.getBookById(bookId);
        cartRedisRepository.putBook(bookId, quantity, sessionId);
    }

    @Override
    public void putBookByMap(Map<Long, Integer> books,String sessionId) {
        cartRedisRepository.putBookByMap(books, sessionId);
    }

    @Override
    public void reduceBookQuantity(Long bookId, String sessionId) {
        cartRedisRepository.reduceBookQuantity(bookId, sessionId);
    }

    @Override
    public void deleteBookFromCart(Long bookId, String sessionId) {
        cartRedisRepository.deleteBookFromCart(bookId, sessionId);
    }

    @Override
    public List<CartBooksResponseDto> lookUpAllBooks(String sessionId) {
        if (sessionId.isBlank()) {
            throw new BadRequestException("SessionId Is Not Valid");
        }


        Map<Long, Integer> booksInRedisCart = getBooksInRedisCart(sessionId);

        if (booksInRedisCart.isEmpty()) {
            List<CartBooksResponseDto> emptyList = new ArrayList<>();
            return emptyList;
        }

        List<Book> booksInfoInRedisCart = bookService.findAllByBookIds(
            booksInRedisCart.keySet().stream().toList());

        List<CartBooksResponseDto> cartBooks = booksInfoInRedisCart.stream()
            .map(book -> new CartBooksResponseDto(book.getPrice(),
                BigDecimal.valueOf(book.getPrice())
                    .multiply(BigDecimal.valueOf(1).subtract(book.getDiscountRate())),
                booksInRedisCart.get(book.getBookId()), book.getStock(), book.getTitle())
            ).toList();

        return cartBooks;
    }



    @Override
    public void setLoginFlag(String sessionId) {
        cartRedisRepository.setLoginFlag(sessionId);
    }
    @Override
    public Boolean getLoginFlag(String sessionId) {
        return cartRedisRepository.getLoginFlag(sessionId);
    }

    @Override
    public void deleteLoginFlag(String sessionId) {
        cartRedisRepository.deleteLoginFlag(sessionId);
    }

    @Override
    public void setCustomerId(String sessionId, String customerLoginId) {
        cartRedisRepository.setCustomerId(sessionId,customerLoginId);
    }

    @Override
    public String getCustomerId(String sessionId) {
        return cartRedisRepository.getCustomerId(sessionId);
    }

    @Override
    public void deleteCustomerId(String sessionId) {
        cartRedisRepository.deleteCustomerId(sessionId);
    }



    @Override
    public Map<Long, Integer> getBooksInRedisCart(String sessionId) {
        Map<Long, Integer> result = new HashMap<>();

        Map<Object, Object> booksInRedisCart = cartRedisRepository.getBooksInRedisCart(sessionId);

        booksInRedisCart.entrySet().stream()
            .forEach(entry -> result.put((Long) entry.getKey(), (Integer) entry.getValue()));

        return result;

    }



    @Override
    public void deleteOldCart(String sessionId) {
        cartRedisRepository.deleteOldCart(sessionId);

    }






}
