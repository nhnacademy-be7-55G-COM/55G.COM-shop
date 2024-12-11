package shop.s5g.shop.entity.review;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.s5g.shop.dto.review.UpdateReviewRequestDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.entity.order.OrderDetail;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    private int score;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime reviewAt;

    private boolean active;

    @OneToMany(mappedBy = "review")
    private List<ReviewImage> reviewImages = new ArrayList<>();

    public Review(Book book, Member member, OrderDetail orderDetail, int score, String content) {
        this.book = book;
        this.member = member;
        this.orderDetail = orderDetail;
        this.score = score;
        this.content = content;
        this.reviewAt = LocalDateTime.now();
        this.active = true;
    }

    public void update(UpdateReviewRequestDto updateReviewRequestDto) {
        this.score = updateReviewRequestDto.score();
        this.content = updateReviewRequestDto.content();
    }
}
