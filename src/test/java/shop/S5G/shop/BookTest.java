package shop.S5G.shop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.repository.BookRepository;
import shop.S5G.shop.service.BookService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookService bookService;

//    @Sql("Book-test.sql")

    /**
     * 도서 등록 Test
     */
    @Test
    void addBookTest() {
        Book book = new Book(
                2,
                22,
                222,
                "아낌없이 주는 나무",
                "전래동화",
                "이 책은 전래동화 입니다.",
                LocalDateTime.of(2000, 10, 10, 10, 50),
                "978-3-15-148410-2",
                15000,
                new BigDecimal("5.5"),
                true,
                200,
                2000,
                LocalDateTime.of(2010, 5, 5, 15, 30)
                );

        Book book1 = bookRepository.save(book);

        int publisher_id = 22;
        Optional<Book> id = bookRepository.findById(book1.getBook_id());
        assertEquals(id.get().getPublisher_id(), publisher_id);
    }

    /**
     * 모든 도서 리스트 조회
     */
    @Test
    void getAllBooksTest() {
        Book book1 = new Book(
                1,
                11,
                111,
                "총균쇠",
                "다큐",
                "이 책은 다큐 입니다.",
                LocalDateTime.of(2000, 10, 10, 10, 50),
                "978-3-15-15859-1",
                20000,
                new BigDecimal("10.0"),
                true,
                200,
                30000,
                LocalDateTime.of(2010, 5, 5, 15, 30)
        );
        Book book2 = new Book(
                2,
                22,
                222,
                "아낌없이 주는 나무",
                "전래동화",
                "이 책은 전래동화 입니다.",
                LocalDateTime.of(2000, 10, 10, 10, 50),
                "978-3-15-148410-2",
                15000,
                new BigDecimal("5.5"),
                true,
                200,
                2000,
                LocalDateTime.of(2010, 5, 5, 15, 30)
        );

        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> books = bookRepository.findAll();
        assertEquals(books.size(), 2);
    }
    /**
     * 도서 상세 조회
     */
    @Test
    void getBookByIdTest() {
        Book book = new Book(
                1,
                11,
                111,
                "총균쇠",
                "다큐",
                "이 책은 다큐 입니다.",
                LocalDateTime.of(2000, 10, 10, 10, 50),
                "978-3-15-15859-1",
                20000,
                new BigDecimal("10.0"),
                true,
                200,
                30000,
                LocalDateTime.of(2010, 5, 5, 15, 30)
        );
        bookRepository.save(book);
        Optional<Book> id = bookRepository.findById(book.getBook_id());
        assertEquals(id.get().getBook_id(), book.getBook_id());
    }

    /**
     * 도서 수정
     */
    @Test
    void updateBookTest() {
        Book book1 = new Book(
                1,
                11,
                111,
                "총균쇠",
                "다큐",
                "이 책은 다큐 입니다.",
                LocalDateTime.of(2000, 10, 10, 10, 50),
                "978-3-15-15859-1",
                20000,
                new BigDecimal("10.0"),
                true,
                200,
                30000,
                LocalDateTime.of(2010, 5, 5, 15, 30)
        );
        Book book2 = new Book(
                1,
                11,
                111,
                "코스모스",
                "다큐",
                "이 책은 다큐 입니다.",
                LocalDateTime.of(2000, 10, 10, 10, 50),
                "978-3-15-15859-1",
                20000,
                new BigDecimal("10.0"),
                true,
                200,
                30000,
                LocalDateTime.of(2010, 5, 5, 15, 30)
        );
        Book save = bookRepository.save(book1);
        bookService.updateBooks(save.getBook_id(), book2);
        assertEquals(save.getTitle(), "코스모스");
    }
}