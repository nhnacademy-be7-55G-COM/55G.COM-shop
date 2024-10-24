package shop.S5G.shop.entity.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import shop.S5G.shop.entity.Book;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_detail_id")
    private long id;

    @ManyToOne
    private Book book;

    @ManyToOne
    private Order order;

    @ManyToOne
    private WrappingPaper wrappingPaper;

    @ManyToOne
    private OrderDetailType orderDetailType;

    private int quantity;
    private long totalPrice;
    private int accumulationPrice;
}
