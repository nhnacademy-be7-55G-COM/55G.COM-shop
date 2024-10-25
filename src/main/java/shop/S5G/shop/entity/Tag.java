package shop.S5G.shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;
    @Column(name = "tag_name")
    private String tagName;
    private boolean active;

    public Tag(String tagName, boolean active) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.active = active;
    }

    public Tag(Long tagId, String tagName, boolean active) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.active = active;
    }
}
