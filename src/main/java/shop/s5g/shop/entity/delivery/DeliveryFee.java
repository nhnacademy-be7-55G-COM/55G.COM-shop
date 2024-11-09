package shop.s5g.shop.entity.delivery;

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
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import shop.s5g.shop.dto.delivery.DeliveryFeeCreateRequestDto;

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

    @Setter
    private long fee;
    @Setter
    @Column(name = "`condition`")
    private long condition;

    // TODO: fee랑 타입이 맞지 않음.
    @Column(name = "refund_delivery_fee")
    @Setter
    private int refundFee;

    @Length(max = 20)
    @Setter
//    @Column(unique = true)
    private String name;

    public DeliveryFee(long fee, long condition, int refundFee, String name) {
        this.fee = fee;
        this.condition = condition;
        this.refundFee = refundFee;
        this.name = name;
    }

    public DeliveryFee(DeliveryFeeCreateRequestDto dto) {
        this(dto.fee(), dto.condition(), dto.refundFee(), dto.name());
    }
}
