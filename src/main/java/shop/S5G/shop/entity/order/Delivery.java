package shop.S5G.shop.entity.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_fee_id")
    private DeliveryFee deliveryFee;

    @Column(name = "delivery_address")
    @Length(max = 100)
    private String address;

    private LocalDate receivedDate;

    @Setter
    private LocalDate shippingDate;

    @Column(name = "delivery_fee")
    private int fee;    // TODO: DeliveryFee.fee 랑 타입이 맞지 않음.

    @Length(max = 20)
    @Setter
    private String invoiceNumber;

    public Delivery(String address, LocalDate receivedDate, int fee) {
        this.address = address;
        this.receivedDate = receivedDate;
        this.fee = fee;
    }
}
