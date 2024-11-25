package shop.s5g.shop.service.review.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.point.PointHistoryCreateRequestDto;
import shop.s5g.shop.dto.point.PointPolicyView;
import shop.s5g.shop.dto.review.ReviewRequestDto;
import shop.s5g.shop.dto.review.ReviewResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.entity.point.PointSource.Name;
import shop.s5g.shop.entity.review.Review;
import shop.s5g.shop.entity.review.ReviewImage;
import shop.s5g.shop.exception.book.BookResourceNotFoundException;
import shop.s5g.shop.exception.member.MemberNotFoundException;
import shop.s5g.shop.exception.order.OrderDetailsNotExistException;
import shop.s5g.shop.exception.review.ReviewAlreadyExistsException;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.member.MemberRepository;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.repository.review.ReviewImageRepository;
import shop.s5g.shop.repository.review.ReviewRepository;
import shop.s5g.shop.service.point.PointHistoryService;
import shop.s5g.shop.service.point.PointPolicyService;
import shop.s5g.shop.service.review.ReviewService;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PointPolicyService pointPolicyService;
    private final PointHistoryService pointHistoryService;

    @Override
    public void registerReview(ReviewRequestDto reviewRequestDto, Long customerId) {
        Book book = bookRepository.findById(reviewRequestDto.bookId())
            .orElseThrow(() -> new BookResourceNotFoundException("존재하지 않는 도서입니다."));

        Member member = memberRepository.findById(customerId)
            .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        OrderDetail orderDetail = orderDetailRepository.findById(
                reviewRequestDto.orderDetailId())
            .orElseThrow(() -> new OrderDetailsNotExistException("존재하지 않는 주문 상세입니다."));

        if (reviewRepository.existsByOrderDetail_id(orderDetail.getId())) {
            throw new ReviewAlreadyExistsException("등록된 리뷰가 존재합니다.");
        }

        Review review = new Review(book, member, orderDetail, reviewRequestDto.score(),
            reviewRequestDto.content());
        reviewRepository.save(review);

        String pointPolicyName = reviewRequestDto.imagePathList().isEmpty() ? "리뷰" : "리뷰-사진";

        PointPolicyView pointPolicy = pointPolicyService.getPolicy(pointPolicyName);

        long pointOffset = pointPolicy.getValue().longValue();

        PointHistoryCreateRequestDto pointHistoryCreateRequestDto = new PointHistoryCreateRequestDto(
            Name.REVIEW.getDataName(), pointOffset);
        pointHistoryService.createPointHistory(customerId, pointHistoryCreateRequestDto);

        // 리뷰 이미지 저장
        if (!reviewRequestDto.imagePathList().isEmpty()) {
            for (String imagePath : reviewRequestDto.imagePathList()) {
                ReviewImage reviewImage = new ReviewImage(review, imagePath);
                reviewImageRepository.save(reviewImage);
            }
        }
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
