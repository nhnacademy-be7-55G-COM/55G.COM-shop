package shop.S5G.shop.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.exception.ResourceNotFoundException;
import shop.S5G.shop.repository.BookRepository;
import shop.S5G.shop.repository.cart.CartRedisRepository;
import shop.S5G.shop.repository.cart.CartRepository;
import shop.S5G.shop.service.cart.impl.CartServiceImpl;



@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
    @Mock
    CartRepository cartRepository;
    @Mock
    BookRepository bookRepository;

    @Mock
    CartRedisRepository cartRedisRepository;

    @Mock
    BookService bookService;

    @InjectMocks
    CartServiceImpl cartServiceImpl;

    Map<Object, Object> booksInRedisCart = new HashMap<>();
    List<Book> booksInfoInRedisCart = new ArrayList<>();




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
    void putBookTest() {
        //given
        Book book = mock(Book.class);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        //when
        assertThatCode(() -> cartServiceImpl.putBook(1L, 1, "testSessionId"))
            .doesNotThrowAnyException();


        //then
        verify(bookRepository, times(1)).findById(1L);
        verify(cartRedisRepository, times(1)).putBook(eq(1L), anyInt(), anyString());
    }

    @Test
    void lookUpAllBooksExceptionTest() {
        //given
        String testSessionId = " ";

        //when
        assertThatThrownBy(() -> cartServiceImpl.lookUpAllBooks(testSessionId)).isInstanceOf(
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

        booksInfoInRedisCart.add(book1);
        booksInfoInRedisCart.add(book2);
        booksInRedisCart.put(book1.getBookId(), 1);
        booksInRedisCart.put(book2.getBookId(), 1);
    }
    @Test
    void lookUpAllBooksTest() throws Exception{
        setupForLookUpAllBooks();
        //given
        String testSessionId = "testSessionId";

        when(bookRepository.findAllById(anyList())).thenReturn(booksInfoInRedisCart);
        when(cartRedisRepository.getBooksInRedisCart(anyString())).thenReturn(booksInRedisCart);


        //when
        assertThatCode(() -> cartServiceImpl.lookUpAllBooks(testSessionId)).doesNotThrowAnyException();

        //then
        verify(bookRepository,times(1)).findAllById(anyList());
        verify(cartRedisRepository,times(1)).getBooksInRedisCart(anyString());
    }

    @Test
    void lookUpAllBooksEmptyTest() {
        //given
        String testSessionId = "testSessionId";
        Map<Object, Object> emptyMap = new HashMap<>();

        //when
        when(cartRedisRepository.getBooksInRedisCart(anyString())).thenReturn(emptyMap);

        //then
        Assertions.assertEquals(cartServiceImpl.lookUpAllBooks(testSessionId).size(), 0);

    }


    @Test
    void putBookByMap() {
        //given
        doNothing().when(cartRedisRepository).putBookByMap(anyMap(), anyString());

        //when
        assertThatCode(
            () -> cartServiceImpl.putBookByMap(anyMap(), anyString())).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository, times(1)).putBookByMap(anyMap(), anyString());
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
        doNothing().when(cartRedisRepository).deleteBookFromCart(anyLong(), anyString());
        //when
        assertThatCode(() -> cartServiceImpl.deleteBookFromCart(anyLong(),
            anyString())).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository).deleteBookFromCart(anyLong(), anyString());

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
    void deleteLoginFlagTest() {
        //given
        doNothing().when(cartRedisRepository).deleteLoginFlag(anyString());

        //when
        assertThatCode(() -> cartServiceImpl.deleteLoginFlag(anyString())).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository, times(1)).deleteLoginFlag(anyString());
    }

    @Test
    void setCustomerIdTest() {
        //given
        doNothing().when(cartRedisRepository).setCustomerId(anyString(), anyString());
        //when
        assertThatCode(
            () -> cartServiceImpl.setCustomerId(anyString(), anyString())).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository, times(1)).setCustomerId(anyString(), anyString());
    }

    @Test
    void deleteCustomerIdTest() {
        //given
        doNothing().when(cartRedisRepository).deleteCustomerId(anyString());

        //when
        assertThatCode(() -> cartServiceImpl.deleteCustomerId(anyString())).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository, times(1)).deleteCustomerId(anyString());

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
}
