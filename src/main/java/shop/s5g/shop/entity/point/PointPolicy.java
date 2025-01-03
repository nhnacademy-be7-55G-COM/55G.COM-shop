package shop.s5g.shop.entity.point;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
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
@Table(name = "point_policy")
public class PointPolicy {
    @Id
    @Column(name = "point_policy_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "point_source_id")
    private PointSource pointSource;

    @Length(max = 50)
    private String name;

    @Setter
    @Column(precision = 10, scale = 2, name = "`value`")
    private BigDecimal value;

    public PointPolicy(PointSource pointSource, String name, BigDecimal value) {
        this.pointSource = pointSource;
        this.name = name;
        this.value = value;
    }
}
