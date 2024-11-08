//package shop.S5G.shop.service.book;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import shop.S5G.shop.dto.Book.BookRequestDto;
//import shop.S5G.shop.entity.Book;
//import shop.S5G.shop.exception.book.BookResourceNotFoundException;
//import shop.S5G.shop.repository.book.BookRepository;
//import shop.S5G.shop.service.book.impl.BookServiceImpl;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class BookServiceTest {
//    @Mock
//    private BookRepository bookRepository;
//    @InjectMocks
//    private BookServiceImpl bookServiceImpl;
//
//    /**
//     * 도서 등록 Test
//     */
//    @Test
//    void addBook() {
//        //given-준비
//        BookRequestDto bookDto = new BookRequestDto(
//                22L,
//                222L,
//                "아낌없이 주는 나무",
//                "전래동화",
//                "이 책은 전래동화 입니다.",
//                LocalDateTime.of(2000, 10, 10, 10, 50),
//                "978-3-15-148410-2",
//                15000L,
//                new BigDecimal("5.5"),
//                true,
//                200,
//                2000L,
//                LocalDateTime.of(2010, 5, 5, 15, 30)
//        );
//
//        //when-실행
//        bookServiceImpl.createBook(bookDto);
//
//        //then-검증
//        verify(bookRepository, times(1)).save(any(Book.class));
//    }
//
//    /**
//     * 도서 상세조회 test
//     */
//    @Test
//    void detailBook() {
//        Book mockBook = mock(Book.class);
//        when(bookRepository.existsById(2L)).thenReturn(false);
////        when(bookRepository.findById(eq(2L))).thenReturn(Optional.empty());
//        assertThatThrownBy(()-> bookServiceImpl.getBookById(2L)).isInstanceOf(BookResourceNotFoundException.class);
//        verify(bookRepository, never()).findById(eq(2L));
//        verify(bookRepository, times(1)).existsById(eq(2L));
//
//    }
//
//    /**
//     * 도서 수정 test
//     */
////    @Test
////    void updateBook() {
////        Book mockBook1 = mock(Book.class);
////        BindingResult bindingResult = mock(BindingResult.class);
////
////        assertThatThrownBy(()->bookService.updateBooks(-3L, mockBook1)).isInstanceOf(BookBadRequestException.class);
////    }
//
//    /**
//     * 도서 삭제 test
//     */
//    @Test
//    void deleteBook() {
//
//        //given
//        Book mockBook = mock(Book.class);
//        when(bookRepository.existsById(2L)).thenReturn(false);
//
//        //when
//        assertThatThrownBy(() -> bookServiceImpl.deleteBooks(2L)).isInstanceOf(BookResourceNotFoundException.class);
//
//        //then
//        verify(bookRepository, never()).findById(eq(2L));
//        verify(bookRepository, times(1)).existsById(eq(2L));
//    }
//
//}
