package shop.s5g.shop.service.review.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.review.CreateReviewRequestDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.entity.review.Review;
import shop.s5g.shop.exception.book.BookResourceNotFoundException;
import shop.s5g.shop.exception.member.MemberNotFoundException;
import shop.s5g.shop.exception.order.OrderDetailsNotExistException;
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

        Review review = new Review(book, member, orderDetail, createReviewRequestDto.score(),
            createReviewRequestDto.content());
        
        reviewRepository.save(review);
    }
}
