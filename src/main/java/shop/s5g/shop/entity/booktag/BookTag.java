package shop.s5g.shop.entity.booktag;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.Tag;

@Entity
@Setter
@Getter
@AllArgsConstructor
@Table(name = "book_tag")
@NoArgsConstructor
public class BookTag {

    @EmbeddedId
    private BookTagId id;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookTag(Book book, Tag tag, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.book = book;
        this.tag = tag;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.id = new BookTagId(book.getBookId(), tag.getTagId());
    }
}
