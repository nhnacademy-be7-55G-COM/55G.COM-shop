package shop.S5G.shop.dto.tag;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TagResponseDto {
    @Size(min = 1, max = 50)
    @NotNull
    private String tagName;
    private boolean active;

    public TagResponseDto() {
    }
    public TagResponseDto(String tagName, boolean active) {
        this.tagName = tagName;
        this.active = active;
    }
}
