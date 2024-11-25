package shop.s5g.shop.entity.refund;

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
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "refund_type")
public class RefundType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_type_id")
    private long id;

    @Column(name = "refund_type_name")
    @Length(max = 20)
    private String name;

    private boolean active;

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        SIMPLE(1, "단순 변심"),
        WRONG_DESTINATION(2, "오배송"),
        DELIVERY_DELAYED(3, "배송 지연"),
        DAMAGED(4, "파손/파본"),
        WRONG_PURCHASE(5, "다른 상품을 잘못 주문함"),
        OTHERS(6, "기타");

        private final long typeId;
        private final String typeName;
    }
}
