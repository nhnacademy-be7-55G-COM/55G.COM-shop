package shop.S5G.shop.dto.Book;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter @Getter
public class BookResponseDto {
    private Long publisherId;
    private Long bookStatusId;
    private String title;
    private String chapter;
    private String description;
    private LocalDateTime publishedDate;
    private String isbn;
    private Long price;
    private BigDecimal discountRate;
    private boolean isPacked;
    private int stock;
    private Long views;
    private LocalDateTime createdAt;
}
