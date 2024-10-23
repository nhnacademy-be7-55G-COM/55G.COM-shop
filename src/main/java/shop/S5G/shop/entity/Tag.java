package shop.S5G.shop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisher_id")
    private Long publisherId;
    @Column(name = "publisher_name")
    private String publisherName;
    private boolean active;

    public Tag(String publisherName, boolean active) {
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.active = active;
    }

    public Tag(Long publisherId, String publisherName, boolean active) {
        this.publisherId = publisherId;
        this.publisherName = publisherName;
        this.active = active;
    }
}
