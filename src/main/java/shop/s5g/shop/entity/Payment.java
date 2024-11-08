package shop.s5g.shop.entity;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import shop.s5g.shop.entity.order.Order;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order orderRelation;

    @Length(max = 200)
    private String paymentKey;

    @Length(max = 10)
    private String currency;

    private long amount;

    // TODO: ??? 언제 컬럼 이름 바뀜?
    @Length(max = 70)
    @Column(name = "payco_order_id")
    private String orderId;

    public Payment(Order orderRelation, String paymentKey, String currency, long amount, String orderId) {
        this.orderRelation=orderRelation;
        this.paymentKey=paymentKey;
        this.currency=currency;
        this.amount=amount;
        this.orderId=orderId;
    }
}
