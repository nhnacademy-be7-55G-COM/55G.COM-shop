package shop.s5g.shop.dto.book;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import shop.s5g.shop.entity.document.BookDocument;

public record BookDocumentResponseDto(
    Long bookId,
    String title,
    String chapter,
    String description,
    LocalDateTime publishedDate,
    String isbn,
    Long price,
    BigDecimal discountRate,
    boolean isPacked,
    int stock,
    Long views,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    String[] authorNames,
    String[] categoryNames,
    String publisherName,
    String[] tagNames,
    String[] imagePaths
) {

    public static BookDocumentResponseDto toDto(BookDocument bookDocument) {
        return new BookDocumentResponseDto(bookDocument.getBook_id(),
            bookDocument.getTitle(), bookDocument.getChapter(), bookDocument.getDescription(),
            bookDocument.getPublished_date(), bookDocument.getIsbn(), bookDocument.getPrice(),
            bookDocument.getDiscount_rate(), bookDocument.is_packed(), bookDocument.getStock(),
            bookDocument.getViews(), bookDocument.getCreated_at(), bookDocument.getUpdated_at(),
            bookDocument.getAuthor_names(), bookDocument.getCategory_names(),
            bookDocument.getPublisher_name(), bookDocument.getTag_names(),
            bookDocument.getImage_paths());
    }
}
