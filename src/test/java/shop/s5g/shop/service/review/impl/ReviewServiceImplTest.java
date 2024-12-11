package shop.s5g.shop.service.review.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.point.PointHistoryCreateRequestDto;
import shop.s5g.shop.dto.point.PointPolicyView;
import shop.s5g.shop.dto.review.CreateReviewRequestDto;
import shop.s5g.shop.dto.review.ReviewResponseDto;
import shop.s5g.shop.dto.review.UpdateReviewRequestDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.BookStatus;
import shop.s5g.shop.entity.Publisher;
import shop.s5g.shop.entity.member.Customer;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.entity.member.MemberGrade;
import shop.s5g.shop.entity.member.MemberStatus;
import shop.s5g.shop.entity.order.Order;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.entity.order.OrderDetailType;
import shop.s5g.shop.entity.order.WrappingPaper;
import shop.s5g.shop.entity.review.Review;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.member.MemberRepository;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.repository.review.ReviewImageRepository;
import shop.s5g.shop.repository.review.ReviewRepository;
import shop.s5g.shop.service.image.ImageService;
import shop.s5g.shop.service.point.PointHistoryService;
import shop.s5g.shop.service.point.PointPolicyService;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewImageRepository reviewImageRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private PointPolicyService pointPolicyService;

    @Mock
    private PointHistoryService pointHistoryService;

    @Mock
    private ImageService imageService;

    private static final String REVIEW_POLICY = "리뷰";
    private static final String REVIEW_WITH_IMAGE_POLICY = "리뷰-사진";

    private final Long customerId = 1L;
    private Book book;
    private Member member;
    private OrderDetail orderDetail;
    private CreateReviewRequestDto createReviewRequestDto;

    @BeforeEach
    public void setUp() {
        book = new Book(
            1L,
            new Publisher(),
            new BookStatus(),
            "Spring",
            "Chapter 1",
            "good book",
            LocalDate.of(2020, 4, 10),
            "978-3-16-148410-0",
            15000L,
            new BigDecimal(0),
            true,
            100,
            0L,
            0L,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        member = new Member(
            1L,
            new Customer(),
            new MemberStatus(),
            new MemberGrade(),
            "user123",
            "password123",
            "1990-01-01",
            LocalDateTime.now(),
            LocalDateTime.now(),
            1000L,
            "payco-12345"
        );

        orderDetail = new OrderDetail(
            1L,
            book,
            new Order(null, null, 0, 0),
            new WrappingPaper(null, 0, null),
            new OrderDetailType(1L, null),
            2,
            5000L,
            250
        );

        createReviewRequestDto = new CreateReviewRequestDto(1L, 1L, 5, "good", List.of(),
            List.of("jpg"));
    }

    @Test
    void registerReview_NoImages() {
        // given
        when(bookRepository.findById(createReviewRequestDto.bookId())).thenReturn(
            Optional.of(book));
        when(memberRepository.findById(customerId)).thenReturn(Optional.of(member));
        when(orderDetailRepository.findById(createReviewRequestDto.orderDetailId())).thenReturn(
            Optional.of(orderDetail));
        when(reviewRepository.existsByOrderDetail_id(orderDetail.getId())).thenReturn(false);

        PointPolicyView pointPolicyView = new PointPolicyView() {
            @Override
            public String getName() {
                return "리뷰";
            }

            @Override
            public BigDecimal getValue() {
                return BigDecimal.valueOf(300);
            }
        };

        String pointPolicyName =
            createReviewRequestDto.imageByteList().isEmpty() ? REVIEW_POLICY
                : REVIEW_WITH_IMAGE_POLICY;

        when(pointPolicyService.getPolicy(pointPolicyName)).thenReturn(pointPolicyView);

        // when
        reviewService.registerReview(createReviewRequestDto, customerId);

        // then
        ArgumentCaptor<Review> reviewCaptor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(reviewCaptor.capture());

        Review savedReview = reviewCaptor.getValue();
        assertThat(savedReview.getBook()).isEqualTo(book);
        assertThat(savedReview.getMember()).isEqualTo(member);
        assertThat(savedReview.getOrderDetail()).isEqualTo(orderDetail);
        assertThat(savedReview.getScore()).isEqualTo(createReviewRequestDto.score());
        assertThat(savedReview.getContent()).isEqualTo(createReviewRequestDto.content());

        verify(pointPolicyService).getPolicy("리뷰");
        verify(pointHistoryService).createPointHistory(eq(1L),
            any(PointHistoryCreateRequestDto.class));
    }

    @Test
    void getReviewList() {
        // given
        Book book2 = new Book(
            2L,
            new Publisher(),
            new BookStatus(),
            "Spring2",
            "Chapter 2",
            "good book",
            LocalDate.of(2020, 4, 10),
            "978-3-16-148410-0",
            15000L,
            new BigDecimal(0),
            true,
            100,
            0L,
            0L,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        OrderDetail orderDetail2 = new OrderDetail(
            2L,
            book,
            new Order(null, null, 0, 0),
            new WrappingPaper(null, 0, null),
            new OrderDetailType(1L, null),
            2,
            5000L,
            250
        );

        Review review1 = new Review(1L, book, member, orderDetail, 3, null, LocalDateTime.now(),
            true, List.of());
        Review review2 = new Review(2L, book2, member, orderDetail2, 5, null, LocalDateTime.now(),
            true, List.of());

        Page<Review> page = new PageImpl<>(Arrays.asList(review1, review2));

        when(reviewRepository.findByMemberIdAndActiveTrue(eq(1L), any(Pageable.class)))
            .thenReturn(page);

        // when
        PageResponseDto<ReviewResponseDto> response = reviewService.getReviewList(1L,
            PageRequest.of(0, 10));

        // then
        assertThat(response.totalElements()).isEqualTo(2);
    }

    @Test
    void updateReview() {
        // given
        Review review = new Review(1L, book, member, orderDetail, 3, null, LocalDateTime.now(),
            true, List.of());

        UpdateReviewRequestDto updateReviewRequestDto = new UpdateReviewRequestDto(1L, 4, "good~",
            List.of(), List.of());

        when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

        // when
        reviewService.updateReview(updateReviewRequestDto, 1L);

        // then
        assertThat(review.getScore()).isEqualTo(4);
        assertThat(review.getContent()).isEqualTo("good~");
    }
}