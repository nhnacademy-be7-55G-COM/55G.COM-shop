package shop.s5g.shop.dto.book;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import shop.s5g.shop.dto.book.category.BookDetailCategoryResponseDto;

public record BookDetailResponseDto(
    long bookId,
    String publisherName,
    String typeName,
    String title,
    String chapter,
    String description,
    LocalDateTime publishedDate,
    String isbn,
    long price,
    BigDecimal discountRate,
    boolean isPacked,
    int stock,
    long views,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<BookAuthorResponseDto> authorList,
    List<BookDetailCategoryResponseDto> categoryList
) {

}
