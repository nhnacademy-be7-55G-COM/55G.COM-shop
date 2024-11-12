package shop.s5g.shop.entity.cart;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.member.Member;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Cart {

    @EmbeddedId
    private CartPk cartPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(value = "bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(value = "customerId")
    @JoinColumn(name = "customer_id")
    private Member member;


    @Setter
    private Integer quantity;
}
