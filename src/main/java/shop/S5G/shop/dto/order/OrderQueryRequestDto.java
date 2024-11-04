package shop.S5G.shop.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record OrderQueryRequestDto(

    long customerId,

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Past
    LocalDate startDate,

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate endDate
) {

}
