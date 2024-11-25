package shop.s5g.shop.service.cart;


import java.util.List;
import java.util.Map;
import shop.s5g.shop.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.shop.dto.cart.request.CartBookSelectRequestDto;
import shop.s5g.shop.dto.cart.request.CartLocalStorageWithStatusRequestDto;
import shop.s5g.shop.dto.cart.request.CartSessionStorageDto;
import shop.s5g.shop.dto.cart.response.CartBooksResponseDto;
import shop.s5g.shop.dto.cart.response.CartDetailInfoResponseDto;
import shop.s5g.shop.entity.cart.Cart;
import shop.s5g.shop.repository.cart.CartFieldValue;

public interface CartService {

    List<Cart> getBooksInDbByCustomerId(String customerLoginId);

    void saveAll(List<Cart> mergedCart);

    void saveMergedCartToRedis(String customerLoginId,List<CartBookInfoRequestDto> cartBookInfoList);

    void FromRedisToDb(String customerLoginId);

    List<CartBooksResponseDto> lookUpAllBooksWhenGuest(
        CartLocalStorageWithStatusRequestDto cartLocalStorageDto);

    void removeAccount(String customerLoginId);

    // ----------- only Redis 관련 -----------
    int getCartCountInRedis(String customerLoginId);

    void deleteBooksAfterPurchase(String customerLoginId);

    Map<Long, CartFieldValue> getBooksInRedisCartWithStatus(String customerLoginId);

    List<CartBookInfoRequestDto> getBooksWhenPurchase(String customerLoginId);

    void changeBookStatus(String customerLoginId,
        CartBookSelectRequestDto cartBookSelectRequestDto);

    Map<Long, Integer> getBooksInRedisCartWithStatusTrue(String customerLoginId);

    void controlQuantity(Long bookId, int change, String customerLoginId);

    int putBook(Long bookId, Integer quantity, String customerLoginId);

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
