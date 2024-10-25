package shop.S5G.shop.dto.tag;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
public class TagRequestDto {
//    private Long publisherId;
    @Size(min = 1, max = 50)
    @NotNull
    private String tagName;
    private boolean active;

    public TagRequestDto() {
    }
    public TagRequestDto(String tagName, boolean active) {
        this.tagName = tagName;
        this.active = active;
    }


}
