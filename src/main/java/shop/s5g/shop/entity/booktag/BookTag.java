package shop.s5g.shop.entity.booktag;

import jakarta.persistence.*;
import lombok.*;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.Tag;

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

    public BookTag(Book book,Tag tag){
        this.book=book;
        this.tag=tag;
        this.id=new BookTagId(book.getBookId(),tag.getTagId());
    }
}
