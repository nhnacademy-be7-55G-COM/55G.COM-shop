package shop.S5G.shop.dto.wrappingpaper;

import shop.S5G.shop.entity.order.WrappingPaper;

public record WrappingPaperResponseDto(long id, String name, int price, String imageName) {
    public static WrappingPaperResponseDto of(WrappingPaper paper) {
        return new WrappingPaperResponseDto(paper.getId(), paper.getName(), paper.getPrice(),
            paper.getImageName());
    }
}
