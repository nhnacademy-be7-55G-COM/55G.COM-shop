package shop.S5G.shop.entity.BookCategory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.entity.Category;

@Entity
@Table(name = "book_category")
@Getter
@Setter
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

    public BookCategory() {}

    public BookCategory(Category category, Book book) {
        this.category = category;
        this.book = book;
    }
}