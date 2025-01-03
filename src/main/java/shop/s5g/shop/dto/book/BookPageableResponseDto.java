package shop.s5g.shop.dto.book;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

//도서목록 페이지에 필요한 도서 정보 Pageable로 보냄
public record BookPageableResponseDto(
        Long bookId,
        Long publisherId,
        Long bookStatusId,
        @NotNull
        String title,
        String chapter,
        String description,
        @NotNull
        LocalDate publishedDate,
        String isbn,
        Long price,
        @NotNull
        BigDecimal discountRate,
        @NotNull
        boolean isPacked,
        @NotNull
        int stock,
        long views,
        @NotNull
        LocalDateTime createdAt,
        String imageName
) {
}
