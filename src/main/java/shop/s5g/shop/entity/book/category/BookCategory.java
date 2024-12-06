package shop.s5g.shop.entity.book.category;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.Category;

@Entity
@Table(name = "book_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BookCategory {

    @EmbeddedId
    private BookCategoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    @MapsId("categoryId")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", insertable = false, updatable = false)
    @MapsId("bookId")
    private Book book;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookCategory(Category category, Book book, LocalDateTime createdAt,
        LocalDateTime updatedAt) {
        this.category = category;
        this.book = book;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.id = new BookCategoryId(book.getBookId(), category.getCategoryId());
    }

}