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
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "delivery_status")
public class DeliveryStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_status_id")
    private long id;

    @Column(name = "type_name")
    @Length(max = 20)
    private String name;

    @Getter
    @RequiredArgsConstructor
    public enum Type {
        PREPARING("배송준비중"), SHIPPING("배송중"), DELIVERED("배송 완료"), CANCELED("배송 취소됨");
        private final String korName;

        @Override
        public String toString() {
            return String.format("[%s(%s)]", name(), korName);
        }
    }
}
