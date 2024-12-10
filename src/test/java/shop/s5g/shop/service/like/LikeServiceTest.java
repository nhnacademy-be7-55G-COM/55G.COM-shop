package shop.s5g.shop.service.like;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.dto.book.BookLikeResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.like.Like;
import shop.s5g.shop.entity.member.Customer;
import shop.s5g.shop.exception.book.BookResourceNotFoundException;
import shop.s5g.shop.exception.like.LikeAlreadyExistsException;
import shop.s5g.shop.exception.like.LikeBadRequestException;
import shop.s5g.shop.exception.member.CustomerNotFoundException;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.like.LikeRepository;
import shop.s5g.shop.repository.member.CustomerRepository;
import shop.s5g.shop.service.book.BookService;
import shop.s5g.shop.service.like.impl.LikeServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {
    @Mock
    private LikeRepository likeRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private LikeServiceImpl likeService;

    /**
     * 도서 내 좋아요 등록 test
     */
    @Test
    @DisplayName("좋아요 등록 성공 테스트")
    void addLikeInBookSuccessTest() {
        // Given
        Long customerId = 1L;
        Long bookId = 1L;

        Customer mockCustomer = Customer.builder()
                .customerId(1L)
                .password("password123")
                .name("John Doe")
                .phoneNumber("010-1234-5678")
                .email("johndoe@example.com")
                .active(true)
                .build();

        Book mockBook = Book.builder()
                .publisher(null) // Publisher 객체를 설정하거나 null로 둠
                .bookStatus(null) // BookStatus 객체를 설정하거나 null로 둠
                .title("Sample Book")
                .chapter("Sample Chapter")
                .description("Sample Description")
                .publishedDate(LocalDate.of(2023, 10, 1))
                .isbn("978-3-16-148410-0")
                .price(15000L)
                .discountRate(new BigDecimal("0.15"))
                .isPacked(true)
                .stock(100)
                .views(500L)
                .createdAt(LocalDateTime.of(2023, 10, 15, 12, 34, 56))
                .updatedAt(LocalDateTime.of(2023, 10, 20, 14, 0, 0))
                .build();

        Like mockLike = new Like(mockCustomer, mockBook);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));
        when(likeRepository.findById(mockLike.getId())).thenReturn(Optional.empty());

        // When
        likeService.addLikeInBook(customerId, bookId);

        // Then
        verify(customerRepository, times(1)).findById(customerId);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).likeCount(bookId);
        verify(likeRepository, times(1)).save(any(Like.class));
    }

    /**
     * 좋아요 등록 실패 test
     */
    @Test
    @DisplayName("좋아요 등록 실패 테스트 - 중복 좋아요")
    void addLikeInBookDuplicateTest() {
        // Given
        Long customerId = 1L;
        Long bookId = 1L;

        Customer mockCustomer = Customer.builder()
                .customerId(1L)
                .password("password123")
                .name("John Doe")
                .phoneNumber("010-1234-5678")
                .email("johndoe@example.com")
                .active(true)
                .build();

        Book mockBook = Book.builder()
                .publisher(null) // Publisher 객체를 설정하거나 null로 둠
                .bookStatus(null) // BookStatus 객체를 설정하거나 null로 둠
                .title("Sample Book")
                .chapter("Sample Chapter")
                .description("Sample Description")
                .publishedDate(LocalDate.of(2023, 10, 1))
                .isbn("978-3-16-148410-0")
                .price(15000L)
                .discountRate(new BigDecimal("0.15"))
                .isPacked(true)
                .stock(100)
                .views(500L)
                .createdAt(LocalDateTime.of(2023, 10, 15, 12, 34, 56))
                .updatedAt(LocalDateTime.of(2023, 10, 20, 14, 0, 0))
                .build();

        Like mockLike = new Like(mockCustomer, mockBook);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));
        when(likeRepository.findById(mockLike.getId())).thenReturn(Optional.of(mockLike));

        // When & Then
        Exception exception = assertThrows(LikeAlreadyExistsException.class,
                () -> likeService.addLikeInBook(customerId, bookId));

        assertEquals("좋아요가 등록된 도서 입니다!", exception.getMessage());
    }

    /**
     * 좋아요 삭제 test
     */
    @Test
    @DisplayName("좋아요 삭제 성공 테스트")
    void deleteLikeInBookSuccessTest() {
        // Given
        Long customerId = 1L;
        Long bookId = 1L;

        Customer mockCustomer = Customer.builder()
                .customerId(1L)
                .password("password123")
                .name("John Doe")
                .phoneNumber("010-1234-5678")
                .email("johndoe@example.com")
                .active(true)
                .build();

        Book mockBook = Book.builder()
                .publisher(null) // Publisher 객체를 설정하거나 null로 둠
                .bookStatus(null) // BookStatus 객체를 설정하거나 null로 둠
                .title("Sample Book")
                .chapter("Sample Chapter")
                .description("Sample Description")
                .publishedDate(LocalDate.of(2023, 10, 1))
                .isbn("978-3-16-148410-0")
                .price(15000L)
                .discountRate(new BigDecimal("0.15"))
                .isPacked(true)
                .stock(100)
                .views(500L)
                .createdAt(LocalDateTime.of(2023, 10, 15, 12, 34, 56))
                .updatedAt(LocalDateTime.of(2023, 10, 20, 14, 0, 0))
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        // When
        likeService.deleteLikeInBook(customerId, bookId);

        // Then
        verify(customerRepository, times(1)).findById(customerId);
        verify(bookRepository, times(1)).findById(bookId);
        verify(likeRepository, times(1)).deleteLike(mockCustomer, mockBook);
    }

    /**
     * 좋아요 삭제 실패 test
     * 잘못된 bookId
     */
    @Test
    @DisplayName("좋아요 삭제 실패 테스트 - 잘못된 customerId")
    void deleteLikeInBookInvalidCustomerIdTest() {
        // Given
        Long customerId = 0L; // Invalid customerId
        Long bookId = 1L;

        // When & Then
        Exception exception = assertThrows(LikeBadRequestException.class,
                () -> likeService.deleteLikeInBook(customerId, bookId));

        assertEquals("customer id는 1보다 커야 합니다.", exception.getMessage());
    }

    /**
     * 좋아요 삭제 실패 test
     * 고객 없음
     */
    @Test
    @DisplayName("좋아요 삭제 실패 테스트 - 고객 없음")
    void deleteLikeInBookCustomerNotFoundTest() {
        // Given
        Long customerId = 1L;
        Long bookId = 1L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(CustomerNotFoundException.class,
                () -> likeService.deleteLikeInBook(customerId, bookId));

        assertEquals("고객을 찾을 수 없습니다.", exception.getMessage());
        verify(customerRepository, times(1)).findById(customerId);
    }

    /**
     * 좋아요 삭제 실패 test
     * 도서 없음
     */
    @Test
    @DisplayName("좋아요 삭제 실패 테스트 - 도서 없음")
    void deleteLikeInBookBookNotFoundTest() {
        // Given
        Long customerId = 1L;
        Long bookId = 1L;

        Customer mockCustomer = Customer.builder()
                .customerId(customerId)
                .password("password123")
                .name("John Doe")
                .phoneNumber("010-1234-5678")
                .email("johndoe@example.com")
                .active(true)
                .build();

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomer));
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(BookResourceNotFoundException.class,
                () -> likeService.deleteLikeInBook(customerId, bookId));

        assertEquals("도서를 찾을 수 없습니다.", exception.getMessage());
        verify(bookRepository, times(1)).findById(bookId);
    }

    /**
     * 좋아요 누른 도서 조회 test
     */
    @Test
    @DisplayName("좋아요 누른 도서 조회 성공 테스트")
    void getLikeBookByCustomerIdSuccessTest() {
        // Given
        Long customerId = 1L;

        List<BookLikeResponseDto> mockLikes = List.of(
                new BookLikeResponseDto(1L, "Book1", 15000L, "Publisher1", "Available"),
                new BookLikeResponseDto(2L, "Book2", 20000L, "Publisher2", "Out of Stock")
        );

        when(likeRepository.getLikeBooksByCustomerId(customerId)).thenReturn(mockLikes);

        // When
        List<BookLikeResponseDto> result = likeService.getLikeBookByCustomerId(customerId);

        // Then
        assertEquals(2, result.size());
        assertEquals("Book1", result.get(0).title());
        assertEquals("Publisher2", result.get(1).publisher());

        verify(likeRepository, times(1)).getLikeBooksByCustomerId(customerId);
    }

}
