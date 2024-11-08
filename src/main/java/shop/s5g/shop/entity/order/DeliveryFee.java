package shop.s5g.shop.entity.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "delivery_fee")
public class DeliveryFee {
    @Id
    @Column(name = "delivery_fee_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long fee;
    private long condition;

    // TODO: fee랑 타입이 맞지 않음.
    @Column(name = "refund_delivery_fee")
    private int refundFee;

    @Length(max = 20)
    // TODO: unique=true?
//    @Column(unique = true)
    private String name;
}
