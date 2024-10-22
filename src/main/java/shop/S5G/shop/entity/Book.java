package shop.S5G.shop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int bookId;
    @Column(name = "publisher_id")
    private int publisherId;
    @Column(name = "book_status_id")
    private int bookStatusId;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String chapter;
    @Column(columnDefinition = "TEXT")
    private String descreption;
    @Column(name = "published_date")
    private LocalDateTime publishedDate;
    private String isbn;
    private int price;
    @Column(name = "discount_rate")
    private BigDecimal discountRate;
    @Column(name = "is_packed")
    private boolean isPacked;
    private int stock;
    private int views;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}