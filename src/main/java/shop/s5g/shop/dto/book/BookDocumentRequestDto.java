package shop.s5g.shop.dto.book;

import jakarta.validation.constraints.NotNull;

public record BookDocumentRequestDto(
    @NotNull
    String keyword
) {

}
