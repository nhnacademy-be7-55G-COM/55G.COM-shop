package shop.S5G.shop.dto.wrappingpaper;

import jakarta.validation.constraints.NotNull;
import shop.S5G.shop.entity.order.WrappingPaper;

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
