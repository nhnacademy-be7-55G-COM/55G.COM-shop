package shop.s5g.shop.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
//@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;
    @Column(name = "tag_name")
    private String tagName;

    @Setter
    private boolean active;

    public Tag(String tagName) {
        this.tagName = tagName;
        this.active = true;
    }

    public Tag(String tagName, boolean active) {
        this.tagName = tagName;
        this.active = active;
    }
}
