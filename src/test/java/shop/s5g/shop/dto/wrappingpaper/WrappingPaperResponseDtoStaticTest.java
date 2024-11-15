package shop.s5g.shop.dto.wrappingpaper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import shop.s5g.shop.entity.order.WrappingPaper;

@DisplayName("포장지 응답 DTO 테스트")
class WrappingPaperResponseDtoStaticTest {
    @Test
    @DisplayName("포장지 응답 of() 메소드 테스트")
    void wrappingPaperResponseDtoStaticMethodTest() {
        WrappingPaper paper = new WrappingPaper(
            123L, "테스트 포장지", 5000, true, "image.png"
        );

        assertThat(WrappingPaperResponseDto.of(paper))
            .hasFieldOrPropertyWithValue("id", paper.getId())
            .hasFieldOrPropertyWithValue("active", paper.isActive())
            .hasFieldOrPropertyWithValue("name", paper.getName())
            .hasFieldOrPropertyWithValue("imageName", paper.getImageName())
            .hasFieldOrPropertyWithValue("price", paper.getPrice());
    }

}
