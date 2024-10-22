package shop.S5G.shop.dto;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookIdDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int book_id;
    private int publisher_id;
    private int book_status_id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String chapter;
    @Column(columnDefinition = "TEXT")
    private String descreption;
    private LocalDateTime published_date;
    private String isbn;
    private int price;
    private BigDecimal discount_rate;
    private boolean is_packed;
    private int stock;
    private int views;
    private LocalDateTime created_at;
}
