package shop.s5g.shop.dto.wrappingpaper;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("포장지 생성 DTO 테스트")
class WrappingPaperRequestDtoStaticMethodTest {
    @Test
    @DisplayName("포장지 생성 요청 toEntity() 테스트")
    void wrappingPaperRequestDtoStaticTest() {
        WrappingPaperRequestDto request = new WrappingPaperRequestDto(
            "테스트", 4000, "image.png"
        );

        assertThat(request.toEntity())
            .hasFieldOrPropertyWithValue("name", request.name())
            .hasFieldOrPropertyWithValue("price", request.price())
            .hasFieldOrPropertyWithValue("imageName", request.imageName())
            .hasFieldOrPropertyWithValue("id", 0L)
            .hasFieldOrPropertyWithValue("active", true);
    }

}
