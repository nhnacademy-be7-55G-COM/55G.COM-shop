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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import shop.S5G.shop.dto.Book.BookResponseDto;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.exception.ResourceNotFoundException;
import shop.S5G.shop.repository.cart.CartRedisRepository;
import shop.S5G.shop.repository.cart.CartRepository;
import shop.S5G.shop.service.book.BookService;
import shop.S5G.shop.service.book.impl.BookServiceImpl;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock
    CartRepository cartRepository;

    @Mock
    CartRedisRepository cartRedisRepository;

    @Mock
    BookService bookService;

    @InjectMocks
    CartService cartService;

    Map<Object, Object> booksInRedisCart = new HashMap<>();
    List<BookResponseDto> booksInfoInRedisCart = new ArrayList<>();

//    @Autowired
    ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();


    @Test
    void putBookExceptionTest() {

        //given
        when(bookService.getBookById(anyLong())).thenThrow(ResourceNotFoundException.class);

        //when
        assertThatThrownBy(() -> cartService.putBook(123L, 1, "testSessionId"))
            .isInstanceOf(ResourceNotFoundException.class);

        //then
        verify(bookService, times(1)).getBookById(anyLong());
        verify(cartRedisRepository, never()).putBook(anyLong(), anyInt(), anyString());

    }

    @Test
    void putBookTest() {
        //given
        BookResponseDto bookResponseDto = mock(BookResponseDto.class);
        when(bookService.getBookById(anyLong())).thenReturn(bookResponseDto);

        //when
        assertThatCode(() -> cartService.putBook(1L, 1, "testSessionId"))
            .doesNotThrowAnyException();


        //then
        verify(bookService, times(1)).getBookById(1L);
        verify(cartRedisRepository, times(1)).putBook(eq(1L), anyInt(), anyString());
    }

    @Test
    void lookUpAllBooksExceptionTest() {
        //given
        String testSessionId = " ";

        //when
        assertThatThrownBy(() -> cartService.lookUpAllBooks(testSessionId)).isInstanceOf(
            BadRequestException.class);

        //then
        verify(bookService, never()).findAllByBookIds(anyList());
        verify(cartRedisRepository, never()).getBooksInRedisCart(anyString());
    }


    void setupForLookUpAllBooks() {
        Book book1 = Book.builder().bookId(1l).price(1000l).discountRate(BigDecimal.valueOf(0.1))
            .stock(10).title("title1").build();
        Book book2 = Book.builder().bookId(2l).price(2000l).discountRate(BigDecimal.valueOf(0.2))
            .stock(20).title("title2").build();
        BookResponseDto b1 = projectionFactory.createProjection(BookResponseDto.class, book1);
        BookResponseDto b2 = projectionFactory.createProjection(BookResponseDto.class, book2);
        booksInfoInRedisCart.add(b1);
        booksInfoInRedisCart.add(b2);
        booksInRedisCart.put(book1.getBookId(), 1);
        booksInRedisCart.put(book2.getBookId(), 1);
    }
    @Test
    void lookUpAllBooksTest() {
        setupForLookUpAllBooks();
        //given
        String testSessionId = "testSessionId";

        when(bookService.findAllByBookIds(anyList())).thenReturn(booksInfoInRedisCart);
        when(cartRedisRepository.getBooksInRedisCart(anyString())).thenReturn(booksInRedisCart);


        //when
        assertThatCode(() -> cartService.lookUpAllBooks(testSessionId)).doesNotThrowAnyException();

        //then
        verify(bookService,times(1)).findAllByBookIds(anyList());
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
        Assertions.assertEquals(cartService.lookUpAllBooks(testSessionId).size(), 0);

    }


    @Test
    void putBookByMap() {
        //given
        doNothing().when(cartRedisRepository).putBookByMap(anyMap(), anyString());

        //when
        assertThatCode(
            () -> cartService.putBookByMap(anyMap(), anyString())).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository, times(1)).putBookByMap(anyMap(), anyString());
    }

    @Test
    void reduceBookQuantityTest() {
        //given
        doNothing().when(cartRedisRepository).reduceBookQuantity(anyLong(), anyString());

        //when
        assertThatCode(() -> cartService.reduceBookQuantity(anyLong(),
            anyString())).doesNotThrowAnyException();


        //then

        verify(cartRedisRepository, times(1)).reduceBookQuantity(anyLong(), anyString());

    }

    @Test
    void deleteBookFromCartTest() {
        //given
        doNothing().when(cartRedisRepository).deleteBookFromCart(anyLong(), anyString());
        //when
        assertThatCode(() -> cartService.deleteBookFromCart(anyLong(),
            anyString())).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository).deleteBookFromCart(anyLong(), anyString());

    }

    @Test
    void setLoginFlagTest() {
        //given
        doNothing().when(cartRedisRepository).setLoginFlag(anyString());

        //when
        assertThatCode(() -> cartService.setLoginFlag(anyString())).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository, times(1)).setLoginFlag(anyString());

    }

    @Test
    void deleteLoginFlagTest() {
        //given
        doNothing().when(cartRedisRepository).deleteLoginFlag(anyString());

        //when
        assertThatCode(() -> cartService.deleteLoginFlag(anyString())).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository, times(1)).deleteLoginFlag(anyString());
    }

    @Test
    void setCustomerIdTest() {
        //given
        doNothing().when(cartRedisRepository).setCustomerId(anyString(), anyLong());
        //when
        assertThatCode(
            () -> cartService.setCustomerId(anyString(), anyLong())).doesNotThrowAnyException();
        //then
        verify(cartRedisRepository, times(1)).setCustomerId(anyString(), anyLong());
    }

    @Test
    void deleteCustomerIdTest() {
        //given
        doNothing().when(cartRedisRepository).deleteCustomerId(anyString());

        //when
        assertThatCode(() -> cartService.deleteCustomerId(anyString())).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository, times(1)).deleteCustomerId(anyString());

    }

    @Test
    void deleteOldCartTest() {
        //given
        doNothing().when(cartRedisRepository).deleteOldCart(anyString());

        //when
        assertThatCode(() -> cartService.deleteOldCart(anyString())).doesNotThrowAnyException();

        //then
        verify(cartRedisRepository, times(1)).deleteOldCart(anyString());
    }
}
