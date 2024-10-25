package shop.S5G.shop.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.exception.BookException.BookAlreadyExistsException;
import shop.S5G.shop.exception.BookException.BookBadRequestException;
import shop.S5G.shop.exception.BookException.BookResourceNotFoundException;
import shop.S5G.shop.repository.BookRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookService bookService;

    /**
     * 도서 등록 Test
     */
    @Test
    void addBook() {
//        //given
        Book mockBook = new Book(
                2L,
                22L,
                222L,
                "아낌없이 주는 나무",
                "전래동화",
                "이 책은 전래동화 입니다.",
                LocalDateTime.of(2000, 10, 10, 10, 50),
                "978-3-15-148410-2",
                15000L,
                new BigDecimal("5.5"),
                true,
                200,
                2000L,
                LocalDateTime.of(2010, 5, 5, 15, 30)
        );
//        bookService.createBook(mockBook);
        Book book = mock(Book.class);
//        when(bookRepository.findById(eq(2L))).thenReturn(Optional.empty());
        //2L인 book이 존재한다고 가정했을 때
        //when({스터빙할 메소드}).{OngoingStubbing 메소드};
        when(bookRepository.findById(eq(2L))).thenReturn(Optional.of(book));

//        Assertions.assertThrows(BookAlreadyExistsException.class, () -> bookService.createBook(mockBook));
        assertThatThrownBy(() -> bookService.createBook(mockBook)).isInstanceOf(BookAlreadyExistsException.class);

        verify(bookRepository, times(1)).findById(eq(2L));
        verify(bookRepository, never()).save(any(Book.class));
//        bookService.getBookByid(2L);
        //when

        //then
    }

    /**
     * 도서 상세조회 test
     */
    @Test
    void detailBook() {
        Book mockBook = mock(Book.class);
        when(bookRepository.existsById(2L)).thenReturn(false);
//        when(bookRepository.findById(eq(2L))).thenReturn(Optional.empty());
        assertThatThrownBy(()-> bookService.getBookById(2L)).isInstanceOf(BookResourceNotFoundException.class);
        verify(bookRepository, never()).findById(eq(2L));
        verify(bookRepository, times(1)).existsById(eq(2L));

    }

    /**
     * 도서 수정 test
     */
//    @Test
//    void updateBook() {
//        Book mockBook1 = mock(Book.class);
//        BindingResult bindingResult = mock(BindingResult.class);
//
//        assertThatThrownBy(()->bookService.updateBooks(-3L, mockBook1)).isInstanceOf(BookBadRequestException.class);
//    }

    /**
     * 도서 삭제 test
     */
    @Test
    void deleteBook() {
        Book mockBook = mock(Book.class);
        when(bookRepository.existsById(2L)).thenReturn(false);
//        when(bookRepository.findById(eq(2L))).thenReturn(Optional.empty());
        assertThatThrownBy(()-> bookService.deleteBooks(2L)).isInstanceOf(BookResourceNotFoundException.class);
        verify(bookRepository, never()).findById(eq(2L));
        verify(bookRepository, times(1)).existsById(eq(2L));
    }

}
