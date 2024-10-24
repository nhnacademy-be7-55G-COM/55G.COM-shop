package shop.S5G.shop.entity.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "delivery_fee_id")
    private DeliveryFee deliveryFee;

    @Column(name = "delivery_address")
    @Length(max = 100)
    private String address;

    private LocalDate receivedDate;
    private LocalDate shippingDate;

    @Column(name = "delivery_fee")
    private int fee;    // TODO: DeliveryFee.fee 랑 타입이 맞지 않음.

    @Length(max = 20)
    private String invoiceNumber;
}
