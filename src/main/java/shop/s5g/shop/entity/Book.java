package shop.s5g.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_status_id")
    private BookStatus bookStatus;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String chapter;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "published_date")
    private LocalDate publishedDate;
    private String isbn;
    private Long price;
    @Column(name = "discount_rate", precision = 10, scale = 2)
    private BigDecimal discountRate;
    @Column(name = "is_packed")
    private boolean isPacked;
    @Setter
    private int stock;
    private Long views;
    private Long likeCount;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    


    @Builder
    public Book(Publisher publisher, BookStatus bookStatus, String title, String chapter,
        String description, LocalDate publishedDate, String isbn, Long price,
        BigDecimal discountRate, boolean isPacked, int stock, Long views, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        this.publisher = publisher;
        this.bookStatus = bookStatus;
        this.title = title;
        this.chapter = chapter;
        this.description = description;
        this.publishedDate = publishedDate;
        this.isbn = isbn;
        this.price = price;
        this.discountRate = discountRate;
        this.isPacked = isPacked;
        this.stock = stock;
        this.views = views;
        this.likeCount = 0L;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}