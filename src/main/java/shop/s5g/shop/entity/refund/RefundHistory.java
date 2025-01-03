package shop.s5g.shop.entity.refund;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import shop.s5g.shop.entity.order.OrderDetail;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "refund_history")
public class RefundHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_history_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_detail_id")
    private OrderDetail orderDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "refund_type_id")
    private RefundType refundType;

    @Length(max = 100)
    @Column(name = "refund_reason")
    private String reason;

    private LocalDateTime refundedAt;
    private boolean active;

    public RefundHistory(OrderDetail detail, RefundType type, String reason) {
        orderDetail = detail;
        refundType = type;
        this.reason = reason;
        refundedAt = LocalDateTime.now();
        active = true;
    }
}
