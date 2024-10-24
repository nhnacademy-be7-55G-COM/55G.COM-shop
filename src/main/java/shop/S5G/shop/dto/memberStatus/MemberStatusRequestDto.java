package shop.S5G.shop.dto.memberStatus;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

public record MemberStatusRequestDto(
    @NotNull
    @NotEmpty
    @Length(max = 15)
    String typeName
) {

}
