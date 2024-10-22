package shop.S5G.shop.dto;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookDto {

//    private Long bookId;
    private Long publisherId;
    private Long bookStatusId;
    private String title;
    private String chapter;
    private String descreption;
    private LocalDateTime publishedDate;
    private String isbn;
    private Long price;
    private BigDecimal discountRate;
    private boolean isPacked;
    private int stock;
    private Long views;
    private LocalDateTime createdAt;
}
