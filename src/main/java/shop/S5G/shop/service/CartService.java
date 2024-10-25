package shop.S5G.shop.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.S5G.shop.dto.cart.response.CartBooksResponseDto;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.repository.cart.CartRedisRepository;
import shop.S5G.shop.repository.cart.CartRepository;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartRedisRepository cartRedisRepository;
    private final BookService bookService;

    public void putBook(Long bookId, Integer quantity, String sessionId) {

        bookService.getBookById(bookId);
        cartRedisRepository.putBook(bookId, quantity, sessionId);
    }

    public void putBookByMap(Map<String, Object> books,String sessionId) {
        cartRedisRepository.putBookByMap(books, sessionId);
    }

    public void reduceBookQuantity(Long bookId, String sessionId, Integer quantity) {
        cartRedisRepository.reduceBookQuantity(bookId, sessionId, quantity);
    }

    public void deleteBookFromCart(Long bookId, String sessionId, Integer quantity) {
        cartRedisRepository.deleteBookFromCart(bookId, sessionId, quantity);
    }

    public List<CartBooksResponseDto> lookUpAllBooks(String sessionId) {
        if (sessionId.isBlank()) {
            throw new BadRequestException("SessionId Is Not Valid");
        }


        Map<Long, Object> booksInRedisCart = getBooksInRedisCart(sessionId);

        if (booksInRedisCart.isEmpty()) {
            List<CartBooksResponseDto> emptyList = new ArrayList<>();
            return emptyList;
        }

        List<Book> booksInfoInRedisCart = bookService.findAllByBookIds(
            booksInRedisCart.keySet().stream().collect(Collectors.toList()));

        List<CartBooksResponseDto> cartBooks = booksInfoInRedisCart.stream()
            .map(book -> new CartBooksResponseDto(book.getPrice(),
                BigDecimal.valueOf(book.getPrice())
                    .multiply(BigDecimal.valueOf(1).subtract(book.getDiscountRate())),
                (Integer) booksInRedisCart.get(book.getBookId()), book.getStock(),book.getTitle())
            ).collect(Collectors.toList());

        return cartBooks;
    }



    public void setLoginFlag(String sessionId) {
        cartRedisRepository.setLoginFlag(sessionId);
    }

    public void deleteLoginFlag(String sessionId) {
        cartRedisRepository.deleteLoginFlag(sessionId);
    }

    public void setCustomerId(String sessionId, Long customerId) {
        cartRedisRepository.setCustomerId(sessionId,customerId);
    }

    public void deleteCustomerId(String sessionId) {
        cartRedisRepository.deleteCustomerId(sessionId);
    }



    public Map<Long, Object> getBooksInRedisCart(String sessionId) {
        Map<Long, Object> result = new HashMap<>();

        Map<Object, Object> booksInRedisCart = cartRedisRepository.getBooksInRedisCart(sessionId);

        booksInRedisCart.entrySet().stream()
            .forEach(entry -> result.put((Long) entry.getKey(), entry.getValue()));

        return result;

    }



    public void deleteOldCart(String sessionId) {
        cartRedisRepository.deleteOldCart(sessionId);

    }






}
