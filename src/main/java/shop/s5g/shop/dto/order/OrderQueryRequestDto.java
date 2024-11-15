package shop.s5g.shop.dto.order;

import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public record OrderQueryRequestDto(
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past
    LocalDate startDate,

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate endDate
) {

}
