package shop.s5g.shop.entity.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.s5g.shop.entity.Book;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_detail_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wrapping_paper_id")
    private WrappingPaper wrappingPaper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_type_id")
    @Setter
    private OrderDetailType orderDetailType;

    private int quantity;
    private long totalPrice;
    private int accumulationPrice;

    @Builder
    public OrderDetail(Book book, Order order, WrappingPaper wrappingPaper, OrderDetailType orderDetailType, int quantity, long totalPrice, int accumulationPrice) {
        this.book = book;
        this.order = order;
        this.wrappingPaper = wrappingPaper;
        this.orderDetailType = orderDetailType;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.accumulationPrice = accumulationPrice;
    }
}
