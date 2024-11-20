package shop.s5g.shop.dto.book;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookRequestDto(
    @NotNull
    Long publisherId,
    @NotNull
    Long bookStatusId,
    @NotNull
    String title,
    String chapter,
    String description,
    @NotNull
    String publishedDate,
    String isbn,
    @NotNull
    Long price,
    @NotNull
    BigDecimal discountRate,
    boolean isPacked,
    @NotNull
    int stock,
    String thumbnailPath   // 도서 이미지가 저장된 URL의 경로
) {

}
