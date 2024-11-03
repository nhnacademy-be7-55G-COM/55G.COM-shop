package shop.S5G.shop.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id")
    private Publisher publisherId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_status_id")
    private BookStatus bookStatusId;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String chapter;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "published_date")
    private LocalDateTime publishedDate;
    private String isbn;
    private Long price;
    @Column(name = "discount_rate")
    private BigDecimal discountRate;
    @Column(name = "is_packed")
    private boolean isPacked;
    private int stock;
    private Long views;
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public Book(Publisher publisherId, BookStatus bookStatusId, String title, String chapter, String description, LocalDateTime publishedDate, String isbn, Long price, BigDecimal discountRate, boolean isPacked, int stock, Long views, LocalDateTime createdAt){
        this.publisherId = publisherId;
        this.bookStatusId = bookStatusId;
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
        this.createdAt = createdAt;
    }
}