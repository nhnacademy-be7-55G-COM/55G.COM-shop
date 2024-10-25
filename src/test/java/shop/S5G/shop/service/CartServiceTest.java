package shop.S5G.shop.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.exception.ResourceNotFoundException;
import shop.S5G.shop.repository.cart.CartRedisRepository;
import shop.S5G.shop.repository.cart.CartRepository;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    CartRepository cartRepository;

    @Mock
    CartRedisRepository cartRedisRepository;

    @Mock
    BookService bookService;

    @InjectMocks
    CartService cartService;

    Map<Object, Object> booksInRedisCart = new HashMap<>();
    List<Book> booksInfoInRedisCart = new ArrayList<>();




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
        Book book = mock(Book.class);
        when(bookService.getBookById(anyLong())).thenReturn(book);

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
        booksInfoInRedisCart.add(book1);
        booksInfoInRedisCart.add(book2);
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
}
