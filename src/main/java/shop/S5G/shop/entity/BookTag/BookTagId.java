package shop.S5G.shop.entity.BookTag;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.*;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.entity.Tag;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Setter
@Getter
public class BookTagId implements Serializable {

    private Long bookId;

    private Long tagId;

    public BookTagId(){}

    public BookTagId(Long bookId, Long tagId) {
        this.bookId = bookId;
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookTagId that = (BookTagId) o;
        return Objects.equals(bookId, that.bookId) && Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, tagId);
    }
}