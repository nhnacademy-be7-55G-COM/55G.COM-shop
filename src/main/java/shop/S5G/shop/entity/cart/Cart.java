package shop.S5G.shop.entity.cart;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.entity.member.Member;

@Entity
public class Cart {
    @EmbeddedId
    private CartPk cartPk;

    @ManyToOne
    @MapsId(value = "bookId")
    private Book book;

    @ManyToOne
    @MapsId(value = "customerId")
    private Member member;

    private Integer quantity;
}
