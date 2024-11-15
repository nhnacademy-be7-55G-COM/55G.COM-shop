package shop.s5g.shop.dto.wrappingpaper;

import shop.s5g.shop.entity.order.WrappingPaper;

public record WrappingPaperResponseDto(long id, boolean active, String name, int price, String imageName) {
    public static WrappingPaperResponseDto of(WrappingPaper paper) {
        return new WrappingPaperResponseDto(paper.getId(), paper.isActive(), paper.getName(), paper.getPrice(),
            paper.getImageName());
    }
}
