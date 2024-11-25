package shop.s5g.shop.entity.like;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class LikeId implements Serializable {

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "book_id")
    private Long bookId;

    public LikeId() {}

    public LikeId(Long customerId, Long bookId) {
        this.customerId = customerId;
        this.bookId = bookId;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        LikeId that = (LikeId) o;
        return Objects.equals(customerId, that.customerId) && Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, bookId);
    }
}
