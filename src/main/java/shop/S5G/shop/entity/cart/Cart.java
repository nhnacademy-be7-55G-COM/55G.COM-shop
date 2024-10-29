package shop.S5G.shop.entity.cart;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.entity.member.Member;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Cart {

    @EmbeddedId
    private CartPk cartPk;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(value = "bookId")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId(value = "customerId")
    private Member member;

    @Setter
    private Integer quantity;
}
