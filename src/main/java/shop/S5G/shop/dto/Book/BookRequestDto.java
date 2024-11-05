package shop.S5G.shop.dto.Book;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import shop.S5G.shop.entity.BookStatus;
import shop.S5G.shop.entity.Publisher;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BookRequestDto (
    @NotNull
//    Publisher publisherId,
    Long publisherId,
    @NotNull
//    BookStatus bookStatusId,
    Long bookStatusId,
    @NotNull
    String title,
    String chapter,
    String description,
    @NotNull
    LocalDateTime publishedDate,
    String isbn,
    @NotNull
    Long price,
    @NotNull
    BigDecimal discountRate,
    @NotNull
    boolean isPacked,
    @NotNull
    int stock,
    @NotNull
    Long views,
    @NotNull
    LocalDateTime createdAt
){
}
