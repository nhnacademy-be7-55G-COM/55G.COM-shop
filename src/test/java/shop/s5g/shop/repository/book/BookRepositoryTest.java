package shop.s5g.shop.repository.book;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import shop.s5g.shop.config.TestQueryFactoryConfig;
import shop.s5g.shop.dto.book.BookPageableResponseDto;
import shop.s5g.shop.dto.book.BookRequestDto;
import shop.s5g.shop.dto.book.BookSimpleResponseDto;
import shop.s5g.shop.entity.*;
import shop.s5g.shop.repository.book.image.BookImageRepository;
import shop.s5g.shop.repository.book.qdsl.impl.BookQuerydslRepositoryImpl;
import shop.s5g.shop.repository.book.status.BookStatusRepository;
import shop.s5g.shop.repository.category.CategoryRepository;
import shop.s5g.shop.repository.publisher.PublisherRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestQueryFactoryConfig.class)
class BookRepositoryTest {
    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookQuerydslRepositoryImpl bookQuerydslRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private BookStatusRepository bookStatusRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private BookImageRepository bookImageRepository;

    @Autowired
    private BookRepository bookJpaRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @Test
    @DisplayName("도서 수정 테스트")
    void updateBookTest() {
        // Given: 연관된 엔티티(Publisher, BookStatus) 먼저 저장
        Publisher publisher = new Publisher("Original Publisher", true);
        publisherRepository.save(publisher);

        BookStatus originalStatus = new BookStatus("Draft");
        BookStatus updatedStatus = new BookStatus("Published");
        bookStatusRepository.save(originalStatus);
        bookStatusRepository.save(updatedStatus);

        // Book 엔티티 저장
        Book book = Book.builder()
                .publisher(publisher)
                .bookStatus(originalStatus)
                .title("Original Title")
                .chapter("Original Chapter")
                .description("Original Description")
                .publishedDate(LocalDate.of(2023, 1, 1))
                .isbn("1234567890123")
                .price(10000L)
                .discountRate(new BigDecimal("10.00"))
                .isPacked(false)
                .stock(50)
                .views(100L)
                .createdAt(LocalDateTime.now().minusDays(10))
                .updatedAt(LocalDateTime.now().minusDays(5))
                .build();
        bookRepository.save(book);

        // When: 도서 정보 업데이트
        BookRequestDto updatedBookDto = new BookRequestDto(
                publisher.getId(),
                updatedStatus.getId(),
                0L, // categoryId가 필요하지 않은 경우
                "Updated Title",
                "Updated Chapter",
                "Updated Description",
                "2023-10-01",
                "9876543210987",
                15000L,
                new BigDecimal("15.00"),
                true,
                100,
                null, // ThumbnailPath
                Collections.emptyList(), // TagIdList
                Collections.emptyList()  // AuthorList
        );

        bookRepository.updateBook(book.getBookId(), updatedBookDto);

        entityManager.flush();
        entityManager.clear();

        // Then: 데이터베이스에서 변경된 데이터를 다시 로드
        Book updatedBook = bookRepository.findById(book.getBookId()).orElseThrow();
        assertEquals(updatedBookDto.title(), updatedBook.getTitle());
        assertEquals(updatedBookDto.chapter(), updatedBook.getChapter());
        assertEquals(updatedBookDto.description(), updatedBook.getDescription());
        assertEquals(LocalDate.parse(updatedBookDto.publishedDate()), updatedBook.getPublishedDate());
        assertEquals(updatedBookDto.isbn(), updatedBook.getIsbn());
        assertEquals(updatedBookDto.price(), updatedBook.getPrice());
        assertEquals(updatedBookDto.discountRate(), updatedBook.getDiscountRate());
        assertEquals(updatedBookDto.isPacked(), updatedBook.isPacked());
        assertEquals(updatedBookDto.stock(), updatedBook.getStock());
    }

    /**
     * 모든도서 test
     */
    @Test
    @DisplayName("모든 도서 조회 테스트")
    void findAllBookPageTest() {
        // Given: 도서와 연관된 엔티티 생성 및 저장
        Publisher publisher = new Publisher("Test Publisher", true);
        publisherRepository.save(publisher);

        BookStatus bookStatus = new BookStatus("Available");
        bookStatusRepository.save(bookStatus);

        Book book1 = Book.builder()
                .publisher(publisher)
                .bookStatus(bookStatus)
                .title("Book 1")
                .chapter("Chapter 1")
                .description("Description 1")
                .publishedDate(LocalDate.of(2023, 1, 1))
                .isbn("1234567890123")
                .price(15000L)
                .discountRate(new BigDecimal("10.00"))
                .isPacked(true)
                .stock(50)
                .views(100L)
                .createdAt(LocalDateTime.now().minusDays(10))
                .updatedAt(LocalDateTime.now().minusDays(5))
                .build();
        Book book2 = Book.builder()
                .publisher(publisher)
                .bookStatus(bookStatus)
                .title("Book 2")
                .chapter("Chapter 2")
                .description("Description 2")
                .publishedDate(LocalDate.of(2023, 2, 1))
                .isbn("9876543210987")
                .price(20000L)
                .discountRate(new BigDecimal("5.00"))
                .isPacked(false)
                .stock(30)
                .views(200L)
                .createdAt(LocalDateTime.now().minusDays(20))
                .updatedAt(LocalDateTime.now().minusDays(10))
                .build();

        bookRepository.save(book1);
        bookRepository.save(book2);

        BookImage bookImage1 = new BookImage(book1, "image1.jpg");
        BookImage bookImage2 = new BookImage(book2, "image2.jpg");
        bookImageRepository.save(bookImage1);
        bookImageRepository.save(bookImage2);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").descending());

        // When: findAllBookPage 호출
        Page<BookPageableResponseDto> result = bookRepository.findAllBookPage(pageable);

        // Then: 반환된 데이터 검증
        assertEquals(2, result.getContent().size());
        assertEquals("Book 2", result.getContent().get(0).title()); // title 기준 내림차순
        assertEquals("Book 1", result.getContent().get(1).title());
        assertEquals("image1.jpg", result.getContent().get(1).imageName());
        assertEquals("image2.jpg", result.getContent().get(0).imageName());
        assertEquals(2, result.getTotalElements()); // 전체 도서 수
    }

    /**
     * 특정 책의 상태 조회 test
     */
    @Test
    @DisplayName("특정 책의 상태 조회 테스트")
    void findBookStatusTest() {
        // Given: 도서와 상태 데이터 생성 및 저장
        BookStatus bookStatus = new BookStatus("Available");
        bookStatusRepository.save(bookStatus);

        Publisher publisher = new Publisher("Test Publisher", true);
        publisherRepository.save(publisher);

        Book book = Book.builder()
                .publisher(publisher)
                .bookStatus(bookStatus)
                .title("Test Book")
                .chapter("Test Chapter")
                .description("Test Description")
                .publishedDate(LocalDate.of(2023, 1, 1))
                .isbn("1234567890123")
                .price(15000L)
                .discountRate(new BigDecimal("10.00"))
                .isPacked(true)
                .stock(50)
                .views(100L)
                .createdAt(LocalDateTime.now().minusDays(10))
                .updatedAt(LocalDateTime.now().minusDays(5))
                .build();

        bookJpaRepository.save(book);

        // When: findBookStatus 호출
        String result = bookRepository.findBookStatus(book.getBookId());

        // Then: 반환된 상태 검증
        assertNotNull(result);
        assertEquals("Available", result);
    }

    /**
     * 도서 간단 조회 test
     */
    @Test
    @DisplayName("특정 ID 리스트로 도서 간단 조회 테스트")
    void findSimpleBooksByIdListTest() {
        // Given: 테스트 데이터 생성 및 저장
        BookStatus availableStatus = new BookStatus("Available");
        bookStatusRepository.save(availableStatus);

        Book book1 = Book.builder()
                .bookStatus(availableStatus)
                .title("Book 1")
                .price(15000L)
                .discountRate(new BigDecimal("10.00"))
                .stock(50)
                .isPacked(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        bookJpaRepository.save(book1);

        Book book2 = Book.builder()
                .bookStatus(availableStatus)
                .title("Book 2")
                .price(20000L)
                .discountRate(new BigDecimal("5.00"))
                .stock(30)
                .isPacked(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        bookJpaRepository.save(book2);

        List<Long> idList = List.of(book1.getBookId(), book2.getBookId());

        // When: findSimpleBooksByIdList 호출
        List<BookSimpleResponseDto> result = bookRepository.findSimpleBooksByIdList(idList);

        // Then: 결과 검증
        assertNotNull(result);
        assertEquals(2, result.size());

        BookSimpleResponseDto bookDto1 = result.stream()
                .filter(dto -> dto.id() == (book1.getBookId()))
                .findFirst()
                .orElseThrow();
        assertEquals("Book 1", bookDto1.title());
        assertEquals(15000L, bookDto1.price());
        assertEquals(new BigDecimal("10.00"), bookDto1.discountRate());
        assertEquals(50, bookDto1.stock());
        assertTrue(bookDto1.isPacked());
        assertEquals("Available", bookDto1.status());

        BookSimpleResponseDto bookDto2 = result.stream()
                .filter(dto -> dto.id() == (book2.getBookId()))
                .findFirst()
                .orElseThrow();
        assertEquals("Book 2", bookDto2.title());
        assertEquals(20000L, bookDto2.price());
        assertEquals(new BigDecimal("5.00"), bookDto2.discountRate());
        assertEquals(30, bookDto2.stock());
        assertFalse(bookDto2.isPacked());
        assertEquals("Available", bookDto2.status());
    }
}
