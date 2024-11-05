package shop.S5G.shop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Author {

    @Id
    private long authorId;

    @OneToOne
    @JoinColumn(name = "profileId")
    private Profile profile;

    private String name;
    private boolean active;
}
