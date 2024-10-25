package shop.S5G.shop.entity.order;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class WrappingPaper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wrapping_paper_id")
    private long id;

    @Length(max = 50)
    private String name;

    private int price;

    @Setter
    private boolean active;

    @Length(max = 50)
    private String imageName;

    public WrappingPaper(String name, int price, String imageName) {
        this.name = name;
        this.price = price;
        this.imageName = imageName;
    }

//    public static WrappingPaper of(WrappingPaperRequestDto dto) {
//        return new WrappingPaper(dto.name(), dto.price(), dto.imageName());
//    }
}
