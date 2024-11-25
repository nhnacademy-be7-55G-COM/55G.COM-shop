package shop.s5g.shop.service.review.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.review.CreateReviewRequestDto;
import shop.s5g.shop.dto.review.ReviewResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.entity.review.Review;
import shop.s5g.shop.exception.book.BookResourceNotFoundException;
import shop.s5g.shop.exception.member.MemberNotFoundException;
import shop.s5g.shop.exception.order.OrderDetailsNotExistException;
import shop.s5g.shop.exception.review.ReviewAlreadyExistsException;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.member.MemberRepository;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.repository.review.ReviewRepository;
import shop.s5g.shop.service.review.ReviewService;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final OrderDetailRepository orderDetailRepository;

    @Override
    public void registerReview(CreateReviewRequestDto createReviewRequestDto, Long customerId) {
        Book book = bookRepository.findById(createReviewRequestDto.bookId())
            .orElseThrow(() -> new BookResourceNotFoundException("존재하지 않는 도서입니다."));

        Member member = memberRepository.findById(customerId)
            .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        OrderDetail orderDetail = orderDetailRepository.findById(
                createReviewRequestDto.orderDetailId())
            .orElseThrow(() -> new OrderDetailsNotExistException("존재하지 않는 주문 상세입니다."));

        if (reviewRepository.existsByOrderDetail_id(orderDetail.getId())) {
            throw new ReviewAlreadyExistsException("등록된 리뷰가 존재합니다.");
        }

        Review review = new Review(book, member, orderDetail, createReviewRequestDto.score(),
            createReviewRequestDto.content());

        reviewRepository.save(review);
    }

    @Override
    public PageResponseDto<ReviewResponseDto> getReviewList(Long memberId, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findByMemberIdAndActiveTrue(memberId, pageable);

        List<ReviewResponseDto> reviewList = reviewPage.getContent().stream()
            .map(ReviewResponseDto::toDto)
            .toList();

        return new PageResponseDto<>(reviewList, reviewPage.getTotalPages(), reviewPage.getSize(),
            reviewPage.getTotalElements());
    }
}
