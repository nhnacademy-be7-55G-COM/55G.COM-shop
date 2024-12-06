package shop.s5g.shop.dto.book;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

//@Builder
public interface BookResponseDto {
    Long getBookId();

    Long getPublisherId();

    Long getBookStatus();

    String getTitle();

    String getChapter();

    String getDescription();

    LocalDate getPublishedDate();

    String getIsbn();

    Long getPrice();

    BigDecimal getDiscountRate();

    boolean isPacked();

    int getStock();

    Long getViews();

    LocalDateTime getCreatedAt();
}