package shop.S5G.shop.entity.BookTag;

import jakarta.persistence.*;
import lombok.*;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.entity.Tag;

@Entity
@Setter
@Getter
@AllArgsConstructor
@Table(name = "book_tag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
