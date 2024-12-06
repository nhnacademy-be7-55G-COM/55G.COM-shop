package shop.s5g.shop.service.review.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.config.ComponentBuilderConfig;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.point.PointHistoryCreateRequestDto;
import shop.s5g.shop.dto.point.PointPolicyView;
import shop.s5g.shop.dto.review.CreateReviewRequestDto;
import shop.s5g.shop.dto.review.ReviewResponseDto;
import shop.s5g.shop.dto.review.UpdateReviewRequestDto;
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
import shop.s5g.shop.exception.review.ReviewNotFoundException;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.member.MemberRepository;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.repository.review.ReviewImageRepository;
import shop.s5g.shop.repository.review.ReviewRepository;
import shop.s5g.shop.service.image.ImageService;
import shop.s5g.shop.service.point.PointHistoryService;
import shop.s5g.shop.service.point.PointPolicyService;
import shop.s5g.shop.service.review.ReviewService;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PointPolicyService pointPolicyService;
    private final PointHistoryService pointHistoryService;
    private final ImageService imageService;

    private static final String REVIEW_POLICY = "리뷰";
    private static final String REVIEW_WITH_IMAGE_POLICY = "리뷰-사진";

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

        // 리뷰 정책에 따라 포인트 적립
        applyReviewPointPolicy(createReviewRequestDto, customerId);

        // 리뷰 이미지 저장
        List<String> imageNames = saveImagesToCloud(createReviewRequestDto.imageByteList(),
            createReviewRequestDto.extensions());
        saveImagesToDB(imageNames, review);
    }

    private void applyReviewPointPolicy(CreateReviewRequestDto reviewRequestDto, Long customerId) {
        String pointPolicyName =
            reviewRequestDto.imageByteList().isEmpty() ? REVIEW_POLICY : REVIEW_WITH_IMAGE_POLICY;

        PointPolicyView pointPolicy = pointPolicyService.getPolicy(pointPolicyName);

        PointHistoryCreateRequestDto pointHistoryCreateRequestDto = new PointHistoryCreateRequestDto(
            Name.REVIEW.getDataName(), pointPolicy.getValue().longValue());
        pointHistoryService.createPointHistory(customerId, pointHistoryCreateRequestDto);
    }

    private List<String> saveImagesToCloud(List<byte[]> imageByteList, List<String> extensions) {
        List<String> imageNames = new ArrayList<>();

        for (int i = 1; i <= imageByteList.size(); i++) {
            byte[] imageByte = imageByteList.get(i - 1);
            String filename = DigestUtils.md5Hex(imageByte);
            String extension = extensions.get(i - 1);

            String digestName = String.format("%s.%s", filename + "-" + i, extension);
            imageService.uploadImage(generateFilePath(digestName), imageByte);

            imageNames.add(digestName);
        }
        return imageNames;
    }

    private String generateFilePath(String digestName) {
        return ComponentBuilderConfig.IMAGE_LOCATION_PATH + "review/" + digestName;
    }


    private void saveImagesToDB(List<String> imageNames, Review review) {
        List<ReviewImage> reviewImagesToSave = imageNames.stream()
            .map(imageName -> new ReviewImage(review, imageName))
            .toList();

        reviewImageRepository.saveAll(reviewImagesToSave);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponseDto<ReviewResponseDto> getReviewList(Long memberId, Pageable pageable) {
        Page<Review> reviewPage = reviewRepository.findByMemberIdAndActiveTrue(memberId, pageable);

        List<ReviewResponseDto> reviewList = reviewPage.getContent().stream()
            .map(ReviewResponseDto::toDto)
            .toList();

        return new PageResponseDto<>(reviewList, reviewPage.getTotalPages(), reviewPage.getSize(),
            reviewPage.getTotalElements());
    }

    @Override
    public void updateReview(UpdateReviewRequestDto updateReviewRequestDto, Long customerId) {
        Review review = reviewRepository.findById(updateReviewRequestDto.reviewId())
            .orElseThrow(() -> new ReviewNotFoundException("존재하지 않는 리뷰입니다."));

        // 평점, 내용 수정
        review.update(updateReviewRequestDto);

        // TODO: CLOUD, DB에 있는 기존 이미지 삭제

        // 리뷰 이미지 저장
        List<String> imageNames = saveImagesToCloud(updateReviewRequestDto.imageByteList(),
            updateReviewRequestDto.extensions());
        saveImagesToDB(imageNames, review);
    }
}
