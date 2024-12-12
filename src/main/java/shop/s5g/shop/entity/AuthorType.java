package shop.s5g.shop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AuthorType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long authorTypeId;
    private String typeName;
    private boolean active;

    public AuthorType(String typeName, boolean active) {
        this.typeName = typeName;
        this.active = active;
    }
}
