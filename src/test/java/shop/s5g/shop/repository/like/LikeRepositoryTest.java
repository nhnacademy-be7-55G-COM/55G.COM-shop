package shop.s5g.shop.repository.like;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.s5g.shop.config.TestQueryFactoryConfig;
import shop.s5g.shop.dto.book.BookLikeResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.BookStatus;
import shop.s5g.shop.entity.Publisher;
import shop.s5g.shop.entity.like.Like;
import shop.s5g.shop.entity.member.Customer;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.book.status.BookStatusRepository;
import shop.s5g.shop.repository.member.CustomerRepository;
import shop.s5g.shop.repository.publisher.PublisherRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestQueryFactoryConfig.class)
class LikeRepositoryTest {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private BookStatusRepository bookStatusRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void getLikeBooksByCustomerIdTest() {
        // Given: 데이터 준비
        Customer customer = new Customer("password123", "John Doe", "010-1234-5678", "john.doe@example.com");
        customer = customerRepository.save(customer);

        Publisher publisher = new Publisher("Sample Publisher", true);
        publisher = publisherRepository.save(publisher);

        BookStatus bookStatus = new BookStatus("Available");
        bookStatus = bookStatusRepository.save(bookStatus);

        Book book = Book.builder()
                .publisher(publisher)
                .bookStatus(bookStatus)
                .title("Sample Book")
                .price(15000L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        book = bookRepository.save(book);

        Like like = new Like(customer, book);
        likeRepository.save(like);

        // When: 좋아요 도서 조회
        List<BookLikeResponseDto> likedBooks = likeRepository.getLikeBooksByCustomerId(customer.getCustomerId());

        // Then: 응답 검증
        assertNotNull(likedBooks);
        assertEquals(1, likedBooks.size());

        BookLikeResponseDto bookDto = likedBooks.get(0);
        assertEquals(book.getBookId(), bookDto.bookId());
        assertEquals(book.getTitle(), bookDto.title());
        assertEquals(book.getPrice(), bookDto.price());
        assertEquals(publisher.getName(), bookDto.publisher());
        assertEquals(bookStatus.getName(), bookDto.status());
    }

    @Test
    void deleteLikeTest() {
        // Given: 테스트 데이터 준비
        Customer customer = new Customer("password123", "John Doe", "010-1234-5678", "john.doe@example.com");
        customer = customerRepository.save(customer);

        Book book = Book.builder()
                .title("Sample Book")
                .price(15000L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        book = bookRepository.save(book);

        Like like = new Like(customer, book);
        likeRepository.save(like);

        // 좋아요 데이터가 존재하는지 확인
        List<Like> existingLikes = likeRepository.findAll();
        assertEquals(1, existingLikes.size());

        // When: 좋아요 삭제
        likeRepository.deleteLike(customer, book);

        // Then: 좋아요 데이터가 삭제되었는지 확인
        List<Like> remainingLikes = likeRepository.findAll();
        assertTrue(remainingLikes.isEmpty());
    }
}
