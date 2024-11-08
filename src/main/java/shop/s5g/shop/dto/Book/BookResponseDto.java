package shop.s5g.shop.dto.Book;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//@Builder
public interface BookResponseDto {
    Long getBookId();
    Long getPublisherId();

    Long getBookStatusId();

    String getTitle();

    String getChapter();

    String getDescription();

    LocalDateTime getPublishedDate();

    String getIsbn();

    Long getPrice();

    BigDecimal getDiscountRate();

    boolean isPacked();

    int getStock();

    Long getViews();

    LocalDateTime getCreatedAt();
}
