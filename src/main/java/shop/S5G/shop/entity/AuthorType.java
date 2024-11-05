package shop.S5G.shop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class AuthorType {

    @Id
    private long authorTypeId;
    private String typeName;
    private boolean active;
}
