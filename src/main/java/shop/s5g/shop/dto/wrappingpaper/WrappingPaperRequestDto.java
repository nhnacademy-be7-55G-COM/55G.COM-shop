package shop.s5g.shop.dto.wrappingpaper;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import shop.s5g.shop.entity.order.WrappingPaper;

public record WrappingPaperRequestDto(
    @Size(min=1, max=50)
    @NotNull
    String name,
    @Min(0)
    int price,
    @Size(min = 1)
    @NotNull
    String imageName
) {
    public WrappingPaper toEntity() {
        return new WrappingPaper(name, price, imageName);
    }
}
