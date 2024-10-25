package shop.S5G.shop.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Setter
@Getter
public class BookTagId implements Serializable {
    @ManyToOne
    private Book book;
    @ManyToOne
    private Tag tag;

    public BookTagId(){}

    public BookTagId(Book book, Tag tag) {
        this.book = book;
        this.tag = tag;
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, tag);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BookTagId other = (BookTagId) obj;
        return Objects.equals(book, other.book) && Objects.equals(tag, other.tag);
    }
}
