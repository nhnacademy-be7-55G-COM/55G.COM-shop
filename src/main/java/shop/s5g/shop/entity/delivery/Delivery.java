package shop.s5g.shop.entity.delivery;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import shop.s5g.shop.entity.order.Order;

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

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Column(name = "delivery_address")
    @Length(max = 100)
    @Setter
    private String address;

    @Setter
    private LocalDate receivedDate;

    @Setter
    private LocalDate shippingDate;

    @Column(name = "delivery_fee")
    private long fee;

    @Length(max = 20)
    @Setter
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_status_id")
    @Setter
    private DeliveryStatus status;

    @Length(max=30)
    private String receiverName;

    // TODO: DELIVERY 수정
    public Delivery(String address, LocalDate receivedDate, DeliveryStatus status, DeliveryFee deliveryFee, String receiverName) {
        this.address = address;
        this.receivedDate = receivedDate;
        this.fee = (int) deliveryFee.getFee();
        this.status=status;
        this.receiverName = receiverName;
        this.deliveryFee = deliveryFee;
    }
}
