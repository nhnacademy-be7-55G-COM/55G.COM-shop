package shop.s5g.shop.repository.book;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import shop.s5g.shop.config.QueryFactoryConfig;
import shop.s5g.shop.dto.book.BookRequestDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.BookStatus;
import shop.s5g.shop.entity.Publisher;
import shop.s5g.shop.exception.book.BookResourceNotFoundException;
import shop.s5g.shop.repository.bookstatus.BookStatusRepository;
import shop.s5g.shop.repository.publisher.PublisherRepository;

@DataJpaTest
@Import(QueryFactoryConfig.class)
class BookRepositoryTest {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final BookStatusRepository bookStatusRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    public BookRepositoryTest(BookRepository bookRepository,
                              PublisherRepository publisherRepository,
                              BookStatusRepository bookStatusRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.bookStatusRepository = bookStatusRepository;
    }

    /**
     * 도서 등록 Test
     */
    @Test
    @DisplayName("도서 등록 test")
    void addBookTest() {

        Publisher publisher = new Publisher();
        BookStatus bookStatus = new BookStatus();

        Book book = new Book(
                publisher,
                bookStatus,
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

        Book save = bookRepository.save(book);
        assertEquals(save.getTitle(), "아낌없이 주는 나무");
    }

    /**
     * 모든 도서 리스트 조회
     */
    //TODO response interface에서 construct생성 불가
//    @Test
//    @DisplayName("모든 도서 리스트 조회")
//    void getAllBooksTest() {
//
//        Publisher publisher1 = new Publisher();
//    Publisher publisher2 = new Publisher();
//
//    BookStatus bookStatus1 = new BookStatus();
//    BookStatus bookStatus2 = new BookStatus();
//        Book book1 = new Book(
//                publisher1,
//                bookStatus1,
//                "총균쇠",
//                "다큐",
//                "이 책은 다큐 입니다.",
//                LocalDateTime.of(2000, 10, 10, 10, 50),
//                "978-3-15-15859-1",
//                20000L,
//                new BigDecimal("10.0"),
//                true,
//                200,
//                30000L,
//                LocalDateTime.of(2010, 5, 5, 15, 30)
//        );
//        Book book2 = new Book(
//                publisher2,
//                bookStatus2,
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
//        bookRepository.save(book1);
//        bookRepository.save(book2);
//
//        List<BookResponseDto> allBookList = bookQuerydslRepository.findAllBookList();
//        assertEquals(allBookList.size(), 2);
//
//    }
    /**
     * 도서 상세 조회
     */
//    @Test
//    void getBookByIdTest() {
//        Book book = new Book(
//                5L,
//                11L,
//                111L,
//                "총균쇠",
//                "다큐",
//                "이 책은 다큐 입니다.",
//                LocalDateTime.of(2000, 10, 10, 10, 50),
//                "978-3-15-15859-1",
//                20000L,
//                new BigDecimal("10.0"),
//                true,
//                200,
//                30000L,
//                LocalDateTime.of(2010, 5, 5, 15, 30)
//        );
//        Book save = bookRepository.save(book);
//        Book id = bookRepository.findById(save.getBookId()).orElseThrow(() -> new BookResourceNotFoundException("Book not found"));
//        assertEquals(id.getTitle(), "총균쇠");
//    }

    /**
     * 도서 수정
     */
    @Test
    @DisplayName("도서 수정 test")
    void updateBookTest() {
        Publisher publisher1 = new Publisher();
        Publisher publisher2 = new Publisher();

        BookStatus bookStatus1 = new BookStatus();
        BookStatus bookStatus2 = new BookStatus();

        publisherRepository.save(publisher1);
        publisherRepository.save(publisher2);
        bookStatusRepository.save(bookStatus1);
        bookStatusRepository.save(bookStatus2);
        Book book1 = new Book(
                publisher1,
                bookStatus1,
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
        BookRequestDto book2 = new BookRequestDto(
                publisher2.getId(),
                bookStatus2.getId(),
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
        bookRepository.updateBook(save.getBookId(), book2);

        testEntityManager.flush();
        testEntityManager.clear();

        Book book = bookRepository.findById(save.getBookId())
                .orElseThrow(() -> new BookResourceNotFoundException("해당 도서는 존재하지 않습니다."));
        Assertions.assertEquals("코스모스", book.getTitle());
    }

    /**
     * 도서 삭제 test
     */
    @Test
    void deleteBookTest() {
        Publisher publisher = new Publisher();
        BookStatus bookStatus = new BookStatus();
        Book book = new Book(
                publisher,
                bookStatus,
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
