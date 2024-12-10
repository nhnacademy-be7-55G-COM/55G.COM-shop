package shop.s5g.shop.dto.author;

import jakarta.validation.constraints.NotBlank;

public record AuthorRequestDto(
        @NotBlank(message = "작가 이름은 필수 입력입니다.")
        String name
) {
}
