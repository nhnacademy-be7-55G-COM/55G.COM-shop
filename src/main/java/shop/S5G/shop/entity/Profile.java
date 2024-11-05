package shop.S5G.shop.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Profile {

    @Id
    private long profileId;

    private String birth;
    private int debutYear;
    private String introduction;
    private String imageName;
    private boolean active;
}
