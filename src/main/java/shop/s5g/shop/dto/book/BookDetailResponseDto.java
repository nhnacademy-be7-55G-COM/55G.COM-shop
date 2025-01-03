package shop.s5g.shop.dto.book;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import shop.s5g.shop.dto.book.author.BookAuthorResponseDto;
import shop.s5g.shop.dto.book.category.BookDetailCategoryResponseDto;
import shop.s5g.shop.dto.tag.TagResponseDto;

public record BookDetailResponseDto(
    long bookId,
    String publisherName,
    String typeName,
    String title,
    String chapter,
    String description,
    LocalDate publishedDate,
    String isbn,
    long price,
    BigDecimal discountRate,
    boolean isPacked,
    int stock,
    long views,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String imagePath,
    List<BookAuthorResponseDto> authorList,
    List<BookDetailCategoryResponseDto> categoryList,
    List<TagResponseDto> tagList,
    long countCoupons
) {

}
