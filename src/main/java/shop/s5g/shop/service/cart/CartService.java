package shop.s5g.shop.service.cart;


import java.util.List;
import java.util.Map;
import shop.s5g.shop.dto.cart.response.CartBooksResponseDto;
import shop.s5g.shop.dto.cart.response.CartDetailInfoResponseDto;
import shop.s5g.shop.entity.cart.Cart;

public interface CartService {

    List<Cart> getBooksInDbByCustomerId(String customerLoginId);

    // Redis 와 Mysql 에 저장되어있는 걸 합쳐서 Db 에 저장
    void saveMergedCartToDb(String sessionId,String customerLoginId);

    // Db 에 있는 걸 Redis 로 복사해준다.
    void transferCartFromDbToRedis(String sessionId, String customerLoginId);

    void saveAll(List<Cart> mergedCart);


    void sessionExpirationInitializing(String sessionId, String customerLoginId);

    // 현재 회원 Db 장바구니를 비우고 redis 에 있는 걸 Db 에 채운다
    void saveRedisToDb(String sessionId, String customerLoginId);

    void deleteAllFromDbCartByCustomerId(Long customerId);



    // ----------- only Redis 관련 -----------

    void controlQuantity(Long bookId, int change, String sessionId);
    void putBook(Long bookId, Integer quantity, String sessionId);

    void putBookByMap(Map<Long, Integer> books,String sessionId);

    void reduceBookQuantity(Long bookId, String sessionId);

    void deleteBookFromCart(Long bookId, String sessionId);

    List<CartBooksResponseDto> lookUpAllBooks(String sessionId);

    CartDetailInfoResponseDto getTotalPriceAndDeliverFee(List<CartBooksResponseDto> cartBooks);


    void setLoginFlag(String sessionId);
    Boolean getLoginFlag(String sessionId);

    void deleteLoginFlag(String sessionId);

    void setCustomerId(String sessionId, String customerLoginId);

    String getCustomerId(String sessionId);

    void deleteCustomerId(String sessionId);



    Map<Long, Integer> getBooksInRedisCart(String sessionId);



    void deleteOldCart(String sessionId);


}
