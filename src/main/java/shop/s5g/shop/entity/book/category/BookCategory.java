package shop.s5g.shop.entity.book.category;

import jakarta.persistence.*;
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
    @JoinColumn(name = "category_id")
    @MapsId("categoryId")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    @MapsId("bookId")
    private Book book;

    public BookCategory(Category category,Book book){
        this.category=category;
        this.book=book;
    }

}