package shop.s5g.shop.dto.Book;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookRequestDto (
    @NotNull
//    Publisher publisherId,
    Long publisherId,
    @NotNull
//    BookStatus bookStatusId,
    Long bookStatusId,
    @NotNull
    String title,
    String chapter,
    String description,
    @NotNull
    LocalDateTime publishedDate,
    String isbn,
    @NotNull
    Long price,
    @NotNull
    BigDecimal discountRate,
    @NotNull
    boolean isPacked,
    @NotNull
    int stock,
    @NotNull
    Long views,
    @NotNull
    LocalDateTime createdAt
){
}
