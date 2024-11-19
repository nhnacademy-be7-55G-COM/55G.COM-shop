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
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "order_detail_type")
public class OrderDetailType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_type_id")
    private long id;

    @Column(name = "type_name")
    @Length(max = 20)
    private String name;

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    public enum Type{
        COMPLETE("주문 완료"), CONFIRM("주문 확정"), RETURN("반품"), CANCEL("주문 취소");
        private final String korName;
    }
}
