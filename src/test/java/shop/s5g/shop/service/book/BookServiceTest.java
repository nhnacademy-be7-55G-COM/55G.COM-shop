package shop.s5g.shop.service.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.repository.book.BookRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    /**
     * 도서 등록 test
     */
//    @Test
//    @DisplayName("도서 등록 test"){
//
//    }

    /**
     * 도서 상세 조회 test
     */
    @Test
    @DisplayName("도서 상세 조회 test")
    void getBookByIdTest() {

    }

}
