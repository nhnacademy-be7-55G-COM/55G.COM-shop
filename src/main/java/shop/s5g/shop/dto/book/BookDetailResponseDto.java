package shop.s5g.shop.dto.book;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
//import lombok.Builder;
import shop.s5g.shop.dto.bookAuthor.BookAuthorResponseDto;
import shop.s5g.shop.dto.bookCategory.BookDetailCategoryResponseDto;

//@Builder
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
    List<BookAuthorResponseDto> authorList,
    List<BookDetailCategoryResponseDto> categoryList
) {

}