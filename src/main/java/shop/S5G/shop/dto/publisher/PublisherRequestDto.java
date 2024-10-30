package shop.S5G.shop.dto.publisher;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


public record PublisherRequestDto (
    @NotNull
    String name,

    @NotNull
    boolean active
){
}
