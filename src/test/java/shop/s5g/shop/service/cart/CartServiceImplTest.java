package shop.s5g.shop.service.cart;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.dto.cart.request.CartBookInfoRequestDto;
import shop.s5g.shop.dto.cart.request.CartBookInfoWithStatusRequestDto;
import shop.s5g.shop.dto.cart.request.CartBookSelectRequestDto;
import shop.s5g.shop.dto.cart.request.CartLocalStorageWithStatusRequestDto;
import shop.s5g.shop.dto.cart.request.CartSessionStorageDto;
import shop.s5g.shop.dto.cart.response.CartBooksInfoInCartResponseDto;
import shop.s5g.shop.dto.cart.response.CartBooksResponseDto;
import shop.s5g.shop.dto.cart.response.CartDetailInfoResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.cart.Cart;
import shop.s5g.shop.entity.cart.CartPk;
import shop.s5g.shop.entity.delivery.DeliveryFee;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.exception.ResourceNotFoundException;

import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.cart.CartFieldValue;
import shop.s5g.shop.repository.cart.CartRedisRepository;
import shop.s5g.shop.repository.cart.CartRepository;
import shop.s5g.shop.repository.delivery.DeliveryFeeRepository;
import shop.s5g.shop.service.cart.impl.CartServiceImpl;
import shop.s5g.shop.service.member.MemberService;


@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
    @Mock
    CartRepository cartRepository;

    @Mock
    BookRepository bookRepository;

    @Mock
    CartRedisRepository cartRedisRepository;

    @Mock
    DeliveryFeeRepository deliveryFeeRepository;

    @Mock
    MemberService memberService;

    @InjectMocks
    CartServiceImpl cartServiceImpl;

    Map<Object, Object> booksInRedisCart = new HashMap<>();
    List<CartBooksInfoInCartResponseDto> booksInfoInRedisCart = new ArrayList<>();

    @Test
    void getBooksInDbByCustomerIdTest() throws Exception {
        //given
        String customerLoginId = "testCustomerId";
        Member member = Member.builder().id(1l).build();

        Book book1 = mock(Book.class);
        Integer quantity = 1;
        Field bookIdField = Book.class.getDeclaredField("bookId");
        bookIdField.setAccessible(true);
        bookIdField.set(book1,1l);

        List<Cart> cart = new ArrayList<>();
        cart.add(new Cart(new CartPk(member.getId(), book1.getBookId()), book1, member, quantity));

        when(memberService.getMember(customerLoginId)).thenReturn(member);
        when(cartRepository.findAllByCartPk_CustomerId(member.getId())).thenReturn(cart);

        //when
        List<Cart> booksInDb = cartServiceImpl.getBooksInDbByCustomerId(customerLoginId);
        //then
        assertEquals(booksInDb, cart);
    }


    @Test
    void saveMergedCartToRedisWhenNormalLogoutTest() throws Exception{
        //given
        String customerId = "testCustomerId";

        Member member = Member.builder().id(1l).build();

        Book book1 = mock(Book.class);
        Integer quantity = 1;
        Field bookIdField = Book.class.getDeclaredField("bookId");
        bookIdField.setAccessible(true);
        bookIdField.set(book1,1l);

        List<Cart> booksInDb = new ArrayList<>();
        booksInDb.add(new Cart(new CartPk(member.getId(), book1.getBookId()), book1, member, quantity));

        when(cartRedisRepository.getLoginFlag(customerId)).thenReturn(null);
        when(memberService.getMember(customerId)).thenReturn(member);
        when(cartRepository.findAllByCartPk_CustomerId(member.getId())).thenReturn(booksInDb);
        when(cartRedisRepository.putBook(book1.getBookId(),quantity,customerId)).thenReturn(1);
        doNothing().when(cartRedisRepository).setLoginFlag(anyString());


        //when
        assertThatCode(() -> cartServiceImpl.saveMergedCartToRedis(customerId,
            List.of(new CartBookInfoRequestDto(book1.getBookId(),
                quantity)))).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository, times(2)).putBook(book1.getBookId(), quantity, customerId);
        verify(cartRedisRepository,times(1)).setLoginFlag(customerId);
        verify(cartRedisRepository,times(1)).getLoginFlag(customerId);

    }

    @Test
    void saveMergedCartToRedisWhenAbnormalLogoutTest() throws Exception{
        //given
        String customerId = "testCustomerId";


        Book book1 = mock(Book.class);
        Integer quantity = 1;
        Field bookIdField = Book.class.getDeclaredField("bookId");
        bookIdField.setAccessible(true);
        bookIdField.set(book1,1l);



        when(cartRedisRepository.getLoginFlag(customerId)).thenReturn(true);
        when(cartRedisRepository.putBook(book1.getBookId(),quantity,customerId)).thenReturn(1);


        //when
        assertThatCode(() -> cartServiceImpl.saveMergedCartToRedis(customerId,
            List.of(new CartBookInfoRequestDto(book1.getBookId(),
                quantity)))).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository, never()).setLoginFlag(customerId);
        verify(cartRedisRepository,times(1)).getLoginFlag(customerId);
        verify(cartRedisRepository, times(1)).putBook(book1.getBookId(), quantity, customerId);


    }

    @Test
    void FromRedisToDbTest() throws NoSuchFieldException, IllegalAccessException {
        String customerLoginId = "testCustomerLoginId";

        Map<Object, Object> booksInRedisCartUnCasted = new HashMap<>();
        booksInRedisCartUnCasted.put(1l, new CartFieldValue(1, true));

        Member member = Member.builder().id(1l).build();

        Book book = mock(Book.class);
        Field bookId = Book.class.getDeclaredField("bookId");
        bookId.setAccessible(true);
        bookId.set(book, 1l);

        List<Cart> booksInDbCart = new ArrayList<>();
        booksInDbCart.add(new Cart(new CartPk(member.getId(), book.getBookId()), book, member, 1));


        when(memberService.getMember(customerLoginId)).thenReturn(member);
        when(cartRedisRepository.getBooksInRedisCart(customerLoginId)).thenReturn(booksInRedisCartUnCasted);
        when(bookRepository.findById(1l)).thenReturn(Optional.of(book));
        when(cartRepository.saveAll(anyList())).thenReturn(booksInDbCart);

        doNothing().when(cartRepository).deleteAllByCartPk_CustomerId(member.getId());
        doNothing().when(cartRedisRepository).deleteOldCart(customerLoginId);
        doNothing().when(cartRedisRepository).deleteLoginFlag(customerLoginId);

        assertThatCode(
            () -> cartServiceImpl.FromRedisToDb(customerLoginId)).doesNotThrowAnyException();

        verify(memberService, times(1)).getMember(customerLoginId);
        verify(cartRedisRepository,times(1)).getBooksInRedisCart(customerLoginId);
        verify(bookRepository,times(1)).findById(1l);
        verify(cartRepository, times(1)).saveAll(anyList());
        verify(cartRepository, times(1)).deleteAllByCartPk_CustomerId(member.getId());
        verify(cartRedisRepository, times(1)).deleteOldCart(customerLoginId);
        verify(cartRedisRepository, times(1)).deleteLoginFlag(customerLoginId);
    }

    @Test
    void saveAllTest() {
        List<Cart> booksInDbCart = new ArrayList<>();
        when(cartRepository.saveAll(anyList())).thenReturn(booksInDbCart);

        assertThatCode(() -> cartServiceImpl.saveAll(anyList())).doesNotThrowAnyException();

        verify(cartRepository, times(1)).saveAll(anyList());

    }

    @Test
    void controlQuantityPutTest() throws NoSuchFieldException, IllegalAccessException {
        String customerLoginId = "testCustomerLoginId";
        int change = 1;
        Book book = mock(Book.class);
        Field bookId = Book.class.getDeclaredField("bookId");
        bookId.setAccessible(true);
        bookId.set(book, 1l);

        when(bookRepository.findById(book.getBookId())).thenReturn(Optional.of(book));
        when(cartRedisRepository.putBook(book.getBookId(),change,customerLoginId)).thenReturn(0);


        assertThatCode(() -> cartServiceImpl.controlQuantity(book.getBookId(), change,
            customerLoginId)).doesNotThrowAnyException();

        verify(bookRepository, times(1)).findById(book.getBookId());
        verify(cartRedisRepository, times(1)).putBook(anyLong(), anyInt(), anyString());
    }

    @Test
    void controlQuantityReduceTest() throws NoSuchFieldException, IllegalAccessException {
        String customerLoginId = "testCustomerLoginId";
        int change = -1;
        Book book = mock(Book.class);
        Field bookId = Book.class.getDeclaredField("bookId");
        bookId.setAccessible(true);
        bookId.set(book, 1l);

        doNothing().when(cartRedisRepository).reduceBookQuantity(anyLong(), anyString());

        assertThatCode(() -> cartServiceImpl.controlQuantity(book.getBookId(), change,
            customerLoginId)).doesNotThrowAnyException();

        verify(cartRedisRepository, times(1)).reduceBookQuantity(anyLong(), anyString());
    }

    @Test
    void controlQuantityValidationFailTest() throws NoSuchFieldException, IllegalAccessException {
        String customerLoginId = "";
        int change = -1;
        Book book = mock(Book.class);
        Field bookId = Book.class.getDeclaredField("bookId");
        bookId.setAccessible(true);
        bookId.set(book, 1l);

        assertThatThrownBy(() -> cartServiceImpl.controlQuantity(book.getBookId(), change,
            customerLoginId)).isInstanceOf(
            BadRequestException.class);

        verify(bookRepository, never()).findById(anyLong());
        verify(cartRedisRepository, never()).putBook(anyLong(), anyInt(), anyString());
        verify(cartRedisRepository, never()).reduceBookQuantity(anyLong(), anyString());
    }

    @Test
    void putBookTest() {
        //given
        Book book = mock(Book.class);
        when(bookRepository.findById(1l)).thenReturn(Optional.of(book));
        when(cartRedisRepository.putBook(1l, 1, "testSessionId")).thenReturn(1);

        //when
        assertThatCode(() -> cartServiceImpl.putBook(1L, 1, "testSessionId"))
            .doesNotThrowAnyException();


        //then
        verify(bookRepository, times(1)).findById(1L);
        verify(cartRedisRepository, times(1)).putBook(anyLong(), anyInt(), anyString());

    }

    @Test
    void putBookExceptionTest() {

        //given
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> cartServiceImpl.putBook(123L, 1, "testSessionId"))
            .isInstanceOf(ResourceNotFoundException.class);

        //then
        verify(bookRepository, times(1)).findById(anyLong());
        verify(cartRedisRepository, never()).putBook(anyLong(), anyInt(), anyString());

    }

    @Test
    void putBookValidationFailTest() {

        //given
        String customerLoginId = "";

        //when
        assertThatThrownBy(() -> cartServiceImpl.putBook(123L, 1, customerLoginId))
            .isInstanceOf(BadRequestException.class);

        //then
        verify(bookRepository, never()).findById(anyLong());
        verify(cartRedisRepository, never()).putBook(anyLong(), anyInt(), anyString());

    }

    @Test
    void reduceBookQuantityTest() {
        //given
        doNothing().when(cartRedisRepository).reduceBookQuantity(anyLong(), anyString());

        //when
        assertThatCode(() -> cartServiceImpl.reduceBookQuantity(anyLong(),
            anyString())).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository, times(1)).reduceBookQuantity(anyLong(), anyString());
    }

    @Test
    void deleteBookFromCartTest() {
        //given
        String customerLoginId = "testCustomerLoginId";
        long bookId = 1l;
        doNothing().when(cartRedisRepository).deleteBookFromCart(bookId, customerLoginId);

        //when
        assertThatCode(() -> cartServiceImpl.deleteBookFromCart(bookId,
            customerLoginId)).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository).deleteBookFromCart(bookId, customerLoginId);

    }

    @Test
    void deleteBookFromCartValidationFailTest() {
        //given
        String customerLoginId = "";
        long bookId = 1l;

        //when
        assertThatThrownBy(
            () -> cartServiceImpl.deleteBookFromCart(bookId, customerLoginId)).isInstanceOf(
            BadRequestException.class);

        //then
        verify(cartRedisRepository, never()).deleteBookFromCart(anyLong(), anyString());

    }

    @Test
    void lookUpAllBooksExceptionTest() {
        //given
        String customerLoginId = "";

        //when
        assertThatThrownBy(() -> cartServiceImpl.lookUpAllBooks(customerLoginId)).isInstanceOf(
            BadRequestException.class);

        //then
        verify(bookRepository, never()).findAllById(anyList());
        verify(cartRedisRepository, never()).getBooksInRedisCart(anyString());
    }


    void setupForLookUpAllBooks() throws Exception {
        Book book1 = Book.builder().price(1000l).discountRate(BigDecimal.valueOf(0.1))
            .stock(10).title("title1").build();
        Book book2 = Book.builder().price(2000l).discountRate(BigDecimal.valueOf(0.2))
            .stock(20).title("title2").build();

        Field bookIdField = Book.class.getDeclaredField("bookId");
        bookIdField.setAccessible(true);
        bookIdField.set(book1, 1L);
        bookIdField.set(book2, 2L);

        booksInfoInRedisCart.add(
            new CartBooksInfoInCartResponseDto(book1.getBookId(), book1.getPrice(),
                book1.getDiscountRate(), book1.getStock(), book1.getTitle(), "image1"));
        booksInfoInRedisCart.add(
            new CartBooksInfoInCartResponseDto(book2.getBookId(), book2.getPrice(),
                book2.getDiscountRate(), book2.getStock(), book2.getTitle(), "image2"));
        booksInRedisCart.put(book1.getBookId(), new CartFieldValue(1, true));
        booksInRedisCart.put(book2.getBookId(), new CartFieldValue(1, true));
    }
    @Test
    void lookUpAllBooksTest() throws Exception{
        setupForLookUpAllBooks();
        //given
        String customerLoginId = "testCustomerLoginId";

        when(bookRepository.findAllBooksInfoInCart(anySet())).thenReturn(booksInfoInRedisCart);
        when(cartRedisRepository.getBooksInRedisCart(anyString())).thenReturn(booksInRedisCart);
        List<CartBooksResponseDto> expectedResult = new ArrayList<>();
        expectedResult.add(
            new CartBooksResponseDto(1l, 1000l, BigDecimal.valueOf(900l).setScale(1), 1, 10, "title1", "image1",
                true));
        expectedResult.add(
            new CartBooksResponseDto(2l, 2000l, BigDecimal.valueOf(1600l).setScale(1), 1, 20, "title2", "image2",
                true));



        //when
        List<CartBooksResponseDto> actualResult = cartServiceImpl.lookUpAllBooks(
            customerLoginId);


        //then
        verify(bookRepository,times(1)).findAllBooksInfoInCart(anySet());
        verify(cartRedisRepository,times(1)).getBooksInRedisCart(anyString());
        Assertions.assertEquals(expectedResult, actualResult);
    }

    @Test
    void lookUpAllBooksEmptyTest() {
        //given
        String customerLoginId = "testCustomerLoginId";
        Map<Object, Object> emptyMap = new HashMap<>();

        //when
        when(cartRedisRepository.getBooksInRedisCart(anyString())).thenReturn(emptyMap);

        //then
        assertEquals(0, cartServiceImpl.lookUpAllBooks(customerLoginId).size());

    }


    @Test
    void lookUpAllBooksWhenGuestTest() throws IllegalAccessException, NoSuchFieldException {
        Book book1 = Book.builder().price(1000l).discountRate(BigDecimal.valueOf(0.1))
            .stock(10).title("title1").build();
        Book book2 = Book.builder().price(2000l).discountRate(BigDecimal.valueOf(0.2))
            .stock(20).title("title2").build();

        Field bookIdField = Book.class.getDeclaredField("bookId");
        bookIdField.setAccessible(true);
        bookIdField.set(book1, 1L);
        bookIdField.set(book2, 2L);

        List<CartBooksInfoInCartResponseDto> booksInfo = new ArrayList<>();
        booksInfo.add(
            new CartBooksInfoInCartResponseDto(book1.getBookId(), book1.getPrice(),
                book1.getDiscountRate(), book1.getStock(), book1.getTitle(), "image1"));
        booksInfo.add(
            new CartBooksInfoInCartResponseDto(book2.getBookId(), book2.getPrice(),
                book2.getDiscountRate(), book2.getStock(), book2.getTitle(), "image2"));

        CartBookInfoWithStatusRequestDto bookInfo1 = new CartBookInfoWithStatusRequestDto(1l, 1,
            true);
        CartBookInfoWithStatusRequestDto bookInfo2 = new CartBookInfoWithStatusRequestDto(2l, 2,
            true);
        List<CartBookInfoWithStatusRequestDto> cartBookInfoList = new ArrayList<>();
        cartBookInfoList.add(bookInfo1);
        cartBookInfoList.add(bookInfo2);
        CartLocalStorageWithStatusRequestDto cartLocalStorageDto = new CartLocalStorageWithStatusRequestDto(cartBookInfoList);

        List<CartBooksResponseDto> expectedResult = new ArrayList<>();
        expectedResult.add(
            new CartBooksResponseDto(1l, 1000l, BigDecimal.valueOf(900l).setScale(1), 1, 10, "title1", "image1",
                true));
        expectedResult.add(
            new CartBooksResponseDto(2l, 2000l, BigDecimal.valueOf(1600l).setScale(1), 2, 20, "title2", "image2",
                true));

        when(bookRepository.findAllBooksInfoInCart(Set.of(1l, 2l))).thenReturn(booksInfo);

        List<CartBooksResponseDto> actualResult = cartServiceImpl.lookUpAllBooksWhenGuest(
            cartLocalStorageDto);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void lookUpAllBooksWhenGuestEmptyTest() throws IllegalAccessException, NoSuchFieldException {

        List<CartBookInfoWithStatusRequestDto> cartBookInfoList = new ArrayList<>();
        CartLocalStorageWithStatusRequestDto cartLocalStorageDto = new CartLocalStorageWithStatusRequestDto(cartBookInfoList);

        assertEquals(0, cartServiceImpl.lookUpAllBooksWhenGuest(cartLocalStorageDto).size());

        verify(bookRepository, never()).findAllBooksInfoInCart(anySet());
    }

    @Test
    void getTotalPriceAndDeliverFeeTest() {
        CartBooksResponseDto bookInfo1 = new CartBooksResponseDto(1l, 1000l,
            BigDecimal.valueOf(900l), 1, 10, "title1", "image",
            true);
        CartBooksResponseDto bookInfo2 = new CartBooksResponseDto(2l, 1000l,
            BigDecimal.valueOf(900l), 1, 10, "title2", "image",
            true);
        List<CartBooksResponseDto> cartBooks = List.of(bookInfo1, bookInfo2);

        DeliveryFee deliveryFee1 = new DeliveryFee(3000l, 0l, 3000, "name1");
        DeliveryFee deliveryFee2 = new DeliveryFee(0l, 30000l, 3000, "name2");
        List<DeliveryFee> deliveryInfo = List.of(deliveryFee1, deliveryFee2);

        when(deliveryFeeRepository.findInfoForPurchase()).thenReturn(deliveryInfo);

        CartDetailInfoResponseDto expectedReturnValue = new CartDetailInfoResponseDto(
            BigDecimal.valueOf(1800l), 3000l, 30000l);

        CartDetailInfoResponseDto actualReturnValue = cartServiceImpl.getTotalPriceAndDeliverFee(
            cartBooks);

        assertEquals(expectedReturnValue, actualReturnValue);
    }

    @Test
    void setLoginFlagTest() {
        //given
        doNothing().when(cartRedisRepository).setLoginFlag(anyString());

        //when
        assertThatCode(() -> cartServiceImpl.setLoginFlag(anyString())).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository, times(1)).setLoginFlag(anyString());

    }

    @Test
    void getLoginFlagTest() {
        String customerLoginId = "testCustomerLoginId";
        when(cartRedisRepository.getLoginFlag(customerLoginId)).thenReturn(true);

        Boolean loginFlag = cartServiceImpl.getLoginFlag(customerLoginId);

        assertEquals(loginFlag, true);
    }

    @Test
    void deleteLoginFlagTest() {
        //given
        doNothing().when(cartRedisRepository).deleteLoginFlag(anyString());

        //when
        assertThatCode(() -> cartServiceImpl.deleteLoginFlag(anyString())).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository, times(1)).deleteLoginFlag(anyString());
    }

    @Test
    void deleteOldCartTest() {
        //given
        doNothing().when(cartRedisRepository).deleteOldCart(anyString());

        //when
        assertThatCode(() -> cartServiceImpl.deleteOldCart(anyString())).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository, times(1)).deleteOldCart(anyString());
    }

    @Test
    void getBooksInRedisCartTest() {
        String customerLoginId = "testCustomerLoginId";

        Map<Long, Integer> expectedResult = new HashMap<>();
        Map<Object, Object> booksInRedisCart = new HashMap<>();
        booksInRedisCart.put(1l, new CartFieldValue(1, true));
        expectedResult.put(1l, 1);

        when(cartRedisRepository.getBooksInRedisCart(customerLoginId)).thenReturn(booksInRedisCart);

        Map<Long, Integer> actualResult = cartServiceImpl.getBooksInRedisCart(customerLoginId);

        assertEquals(expectedResult,actualResult);
    }

    @Test
    void getBooksInRedisCartWithStatusTest() {
        //given
        String customerLoginId = "testCustomerLoginId";

        Map<Object, Object> booksInRedisCart = new HashMap<>();
        booksInRedisCart.put(1l, new CartFieldValue(1, true));

        Map<Long, CartFieldValue> expectedResult = new HashMap<>();
        expectedResult.put(1l, new CartFieldValue(1, true));

        when(cartRedisRepository.getBooksInRedisCart(customerLoginId)).thenReturn(booksInRedisCart);

        //when
        Map<Long, CartFieldValue> actualResult = cartServiceImpl.getBooksInRedisCartWithStatus(
            customerLoginId);

        //then
        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getBooksInRedisCartWithStatusTrueTest() {
        String customerLoginId = "testCustomerLoginId";

        Map<Object, Object> booksInRedisCart = new HashMap<>();
        booksInRedisCart.put(1l, new CartFieldValue(1, true));
        booksInRedisCart.put(2l, new CartFieldValue(1, false));

        Map<Long, Integer> expectedResult = new HashMap<>();
        expectedResult.put(1l, 1);

        when(cartRedisRepository.getBooksInRedisCart(customerLoginId)).thenReturn(booksInRedisCart);

        Map<Long, Integer> actualResult = cartServiceImpl.getBooksInRedisCartWithStatusTrue(
            customerLoginId);

        assertEquals(expectedResult, actualResult);

    }

    @Test
    void removeAccountTest() {
        String customerLoginId = "testCustomerLoginId";
        Member member = Member.builder().loginId(customerLoginId).id(1l).build();

        when(memberService.getMember(customerLoginId)).thenReturn(member);
        doNothing().when(cartRedisRepository).deleteOldCart(customerLoginId);
        doNothing().when(cartRepository).deleteAllByCartPk_CustomerId(anyLong());
        doNothing().when(cartRedisRepository).deleteLoginFlag(customerLoginId);

        assertThatCode(
            () -> cartServiceImpl.removeAccount(customerLoginId)).doesNotThrowAnyException();

        verify(cartRedisRepository, times(1)).deleteOldCart(customerLoginId);
        verify(cartRepository, times(1)).deleteAllByCartPk_CustomerId(anyLong());
        verify(cartRedisRepository, times(1)).deleteLoginFlag(customerLoginId);


    }

    @Test
    void removeAccountValidationFailTest() {
        String customerLoginId = "";

        assertThatThrownBy(() -> cartServiceImpl.removeAccount(customerLoginId)).isInstanceOf(
            BadRequestException.class);

        verify(cartRedisRepository, never()).deleteOldCart(customerLoginId);
        verify(cartRepository, never()).deleteAllByCartPk_CustomerId(anyLong());
        verify(cartRedisRepository, never()).deleteLoginFlag(customerLoginId);
    }

    @Test
    void getBooksWhenPurchaseTest() {
        String customerLoginId = "testCustomerLoginId";

        Map<Object, Object> booksInRedisCart = new HashMap<>();
        booksInRedisCart.put(1l, new CartFieldValue(1, true));
        booksInRedisCart.put(2l, new CartFieldValue(1, false));
        CartBookInfoRequestDto cartBookInfoRequestDto = new CartBookInfoRequestDto(1l, 1);
        List<CartBookInfoRequestDto> expectedResult = List.of(cartBookInfoRequestDto);

        when(cartRedisRepository.getBooksInRedisCart(customerLoginId)).thenReturn(booksInRedisCart);

        List<CartBookInfoRequestDto> actualResult = cartServiceImpl.getBooksWhenPurchase(
            customerLoginId);

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void getBooksWhenPurchaseValidationFailTest() {
        String customerLoginId = "";

        assertThatThrownBy(
            () -> cartServiceImpl.getBooksWhenPurchase(customerLoginId)).isInstanceOf(
            BadRequestException.class);

        verify(cartRedisRepository, never()).getBooksInRedisCart(customerLoginId);
    }

    @Test
    void deleteBooksAfterPurchaseTest() {
        String customerLoginId = "testCustomerLoginId";

        Map<Object, Object> booksInRedisCart = new HashMap<>();
        booksInRedisCart.put(1l, new CartFieldValue(1, true));
        booksInRedisCart.put(2l, new CartFieldValue(1, false));

        when(cartRedisRepository.getBooksInRedisCart(customerLoginId)).thenReturn(booksInRedisCart);
        doNothing().when(cartRedisRepository).deleteBookFromCart(anyLong(), anyString());

        assertThatCode(() -> cartServiceImpl.deleteBooksAfterPurchase(
            customerLoginId)).doesNotThrowAnyException();

        verify(cartRedisRepository, times(1)).getBooksInRedisCart(customerLoginId);
        verify(cartRedisRepository, times(1)).deleteBookFromCart(anyLong(), anyString());

    }


    @Test
    void deleteBooksAfterPurchaseValidationFailTest() {
        String customerLoginId = "";

        assertThatThrownBy(
            () -> cartServiceImpl.deleteBooksAfterPurchase(customerLoginId)).isInstanceOf(
            BadRequestException.class);


        verify(cartRedisRepository, never()).getBooksInRedisCart(customerLoginId);
        verify(cartRedisRepository, never()).deleteBookFromCart(anyLong(), anyString());

    }


    @Test
    void changeBookStatusTest() {
        String customerLoginId = "testCustomerLoginId";
        CartBookSelectRequestDto cartBookSelectRequestDto = new CartBookSelectRequestDto(1l, true);

        doNothing().when(cartRedisRepository)
            .changeBookStatus(customerLoginId, cartBookSelectRequestDto);

        assertThatCode(() -> cartServiceImpl.changeBookStatus(customerLoginId,
            cartBookSelectRequestDto)).doesNotThrowAnyException();

        verify(cartRedisRepository, times(1)).changeBookStatus(customerLoginId,
            cartBookSelectRequestDto);

    }

    @Test
    void changeBookStatusValidationFailTest() {
        String customerLoginId = "";
        CartBookSelectRequestDto cartBookSelectRequestDto = new CartBookSelectRequestDto(1l, true);

        assertThatThrownBy(() -> cartServiceImpl.changeBookStatus(customerLoginId,
            cartBookSelectRequestDto)).isInstanceOf(
            BadRequestException.class);

        verify(cartRedisRepository, never()).changeBookStatus(customerLoginId,
            cartBookSelectRequestDto);

    }






}
