package shop.S5G.shop.entity.bookcategory;

import jakarta.persistence.*;
import lombok.*;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.entity.Category;

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

}