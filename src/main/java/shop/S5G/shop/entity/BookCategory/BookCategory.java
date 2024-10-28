package shop.S5G.shop.entity.BookCategory;

import jakarta.persistence.*;
import lombok.*;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.entity.Category;

@Entity
@Table(name = "book_category")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BookCategory {

    @EmbeddedId
    private BookCategoryId id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    @MapsId("categoryId")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @MapsId("bookId")
    private Book book;

}