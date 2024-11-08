package shop.S5G.shop.service.cart;


import java.util.List;
import java.util.Map;
import shop.S5G.shop.dto.cart.request.CartBookInfoRequestDto;
import shop.S5G.shop.dto.cart.request.CartSessionStorageDto;
import shop.S5G.shop.dto.cart.response.CartBooksResponseDto;
import shop.S5G.shop.dto.cart.response.CartDetailInfoResponseDto;
import shop.S5G.shop.entity.cart.Cart;

public interface CartService {

    List<Cart> getBooksInDbByCustomerId(String customerLoginId);

    void saveAll(List<Cart> mergedCart);

    void saveMergedCartToRedis(String customerLoginId,List<CartBookInfoRequestDto> cartBookInfoList);

    void FromRedisToDb(String customerLoginId);

    List<CartBooksResponseDto> lookUpAllBooksWhenGuest(CartSessionStorageDto cartSessionStorageDto);

    // ----------- only Redis 관련 -----------

    void controlQuantity(Long bookId, int change, String customerLoginId);

    void putBook(Long bookId, Integer quantity, String customerLoginId);

    void reduceBookQuantity(Long bookId, String customerLoginId);

    void deleteBookFromCart(Long bookId, String customerLoginId);

    List<CartBooksResponseDto> lookUpAllBooks(String customerLoginId);

    CartDetailInfoResponseDto getTotalPriceAndDeliverFee(List<CartBooksResponseDto> cartBooks);

    Boolean getLoginFlag(String sessionId);

    void setLoginFlag(String sessionId);

    void deleteLoginFlag(String sessionId);

    Map<Long, Integer> getBooksInRedisCart(String customerLoginId);

    void deleteOldCart(String customerLoginId);
}
