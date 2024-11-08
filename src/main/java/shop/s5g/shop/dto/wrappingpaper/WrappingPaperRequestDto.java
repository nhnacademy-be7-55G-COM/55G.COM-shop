package shop.s5g.shop.dto.wrappingpaper;

import jakarta.validation.constraints.NotNull;
import shop.s5g.shop.entity.order.WrappingPaper;

public record WrappingPaperRequestDto(
    @NotNull
    String name,
    int price,
    String imageName
) {
    public WrappingPaper toEntity() {
        return new WrappingPaper(name, price, imageName);
    }
}
