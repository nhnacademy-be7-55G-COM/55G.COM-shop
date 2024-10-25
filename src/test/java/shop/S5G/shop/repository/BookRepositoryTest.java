package shop.S5G.shop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.exception.BookException.BookResourceNotFoundException;

@DataJpaTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookRepositoryTest {

    private final BookRepository bookRepository;

    @Autowired
    public BookRepositoryTest(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * 도서 등록 Test
     */
    @Test
    void addBookTest() {
        Book book = new Book(
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

        Book book1 = bookRepository.save(book);

        int publisher_id = 22;
        Optional<Book> id = bookRepository.findById(book1.getBookId());
        assertEquals(id.get().getPublisherId(), publisher_id);
    }

    /**
     * 모든 도서 리스트 조회
     */
    @Test
    void getAllBooksTest() {
        Book book1 = new Book(
                1L,
                11L,
                111L,
                "총균쇠",
                "다큐",
                "이 책은 다큐 입니다.",
                LocalDateTime.of(2000, 10, 10, 10, 50),
                "978-3-15-15859-1",
                20000L,
                new BigDecimal("10.0"),
                true,
                200,
                30000L,
                LocalDateTime.of(2010, 5, 5, 15, 30)
        );
        Book book2 = new Book(
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
                5L,
                11L,
                111L,
                "총균쇠",
                "다큐",
                "이 책은 다큐 입니다.",
                LocalDateTime.of(2000, 10, 10, 10, 50),
                "978-3-15-15859-1",
                20000L,
                new BigDecimal("10.0"),
                true,
                200,
                30000L,
                LocalDateTime.of(2010, 5, 5, 15, 30)
        );
        Book save = bookRepository.save(book);
        Book id = bookRepository.findById(save.getBookId()).orElseThrow(()-> new BookResourceNotFoundException("Book not found"));
        assertEquals(id.getTitle(), "총균쇠");
    }

    /**
     * 도서 수정
     */
    @Test
    void updateBookTest() {
        Book book1 = new Book(
                1L,
                11L,
                111L,
                "총균쇠",
                "다큐",
                "이 책은 다큐 입니다.",
                LocalDateTime.of(2000, 10, 10, 10, 50),
                "978-3-15-15859-1",
                20000L,
                new BigDecimal("10.0"),
                true,
                200,
                30000L,
                LocalDateTime.of(2010, 5, 5, 15, 30)
        );
        Book book2 = new Book(
                1L,
                11L,
                111L,
                "코스모스",
                "다큐",
                "이 책은 다큐 입니다.",
                LocalDateTime.of(2000, 10, 10, 10, 50),
                "978-3-15-15859-1",
                20000L,
                new BigDecimal("10.0"),
                true,
                200,
                30000L,
                LocalDateTime.of(2010, 5, 5, 15, 30)
        );
        Book save = bookRepository.save(book1);
        Book book = bookRepository.findById(book1.getBookId()).orElseThrow(()->new BookResourceNotFoundException("찾을 수 없습니다."));

        book.setBookId(book2.getBookId());
        book.setPublisherId(book2.getPublisherId());
        book.setBookStatusId(book2.getBookStatusId());
        book.setTitle(book2.getTitle());
        book.setChapter(book2.getChapter());
        book.setDescreption(book2.getDescreption());
        book.setPublishedDate(book2.getPublishedDate());
        book.setIsbn(book2.getIsbn());
        book.setPrice(book2.getPrice());
        book.setDiscountRate(book2.getDiscountRate());
        book.setPacked(book2.isPacked());
        book.setStock(book2.getStock());
        book.setViews(book2.getViews());
        book.setCreatedAt(book2.getCreatedAt());

        Book book3 = bookRepository.save(book);
        assertEquals(book3.getTitle(), "코스모스");
    }

    /**
     * 도서 삭제 test
     */
    @Test
    void deleteBookTest() {
        Book book = new Book(
                2L,
                11L,
                111L,
                "총균쇠",
                "다큐",
                "이 책은 다큐 입니다.",
                LocalDateTime.of(2000, 10, 10, 10, 50),
                "978-3-15-15859-1",
                20000L,
                new BigDecimal("10.0"),
                true,
                200,
                30000L,
                LocalDateTime.of(2010, 5, 5, 15, 30)
        );
        bookRepository.save(book);
        bookRepository.delete(book);
//        List<Book> books = bookRepository.findAll();
        assertEquals(bookRepository.count(), 0);
    }
}
