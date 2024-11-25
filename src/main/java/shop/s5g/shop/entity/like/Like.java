package shop.s5g.shop.entity.like;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.member.Customer;

@Entity
@Getter
@AllArgsConstructor
@Table(name = "like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @EmbeddedId
    private LikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @MapsId("customerId")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    @MapsId("bookId")
    private Book book;

    public Like(Customer customer, Book book) {
        this.customer = customer;
        this.book = book;
    }

}
