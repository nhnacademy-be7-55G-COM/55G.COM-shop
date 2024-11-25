package shop.s5g.shop.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ReviewRequestDto(
    @NotNull
    Long bookId,

    @NotNull
    Long orderDetailId,

    @Min(1)
    @Max(5)
    int score,

    String content,
    List<String> imagePathList
) {

}
